BaseDir=${1}
JarPath=${2}
BuildNum=${3}
JobName=${4}
JobEnv=${5}
LoadBalanceIp=${6}
ExposedPort=${7}
RegistryUrl=${8}

LocalIp=$(hostname -I | awk '{print $1}')
DevopsPath=/var/lib/jenkins/devops
HomeHostName=$(hostname)
DockerJob=${RegistryUrl}/cloudhos/${JobEnv}/2.0/${JobName}
DockerImage=${DockerJob}:${BuildNum}
deploymentName=$(echo ${JobName} | sed 's|_|-|g' | sed 's|\.|-|g')  # deploymentName=management-test

# Compile into a jar package
DefaultMvnInstall="mvn -T 8C -Dmaven.compile.fork=true -Dmaven.test.skip=true clean package -pl ${BaseDir}/${JarPath} -am -DforceStdout -q"

# Package and upload images
docker build -t ${DockerImage} .
docker push "${DockerImage}"
docker rmi -f $(docker images ${DockerJob} -q)

# env， When passing a variable through envsubst, the variable must be declared below
export RegistryUrl=${RegistryUrl}
export JobName=${JobName}
export deploymentName=${deploymentName}
export JobEnv=${JobEnv}
export BuildNum=${BuildNum}
export LoadBalanceIp=${LoadBalanceIp}
export NameSpace=${NameSpace}

# kustomize
kustomizeBuild() {
    echoYellow "kustomize build ${1}"
    echoGreen "ExposedPort:"${ExposedPort}
    echoBuleNoTime "YAML FILES HEAD"
    kustomize build $1 | envsubst
    echoBuleNoTime "YAML FILES TAIL"
    kustomize build $1 | envsubst > ./${deploymentName}.yaml
    kustomize build $1 | envsubst | kubectl apply -f - || { echoRed "kustomization deploy failed"; exit 1; }
    echoGreen kustomization deply success!
}

# Environment deployment
TestDeploy() {
    cd /var/lib/jenkins/devops/
    git pull
    cd -

    if [ -z "$(kubectl get namespace | grep ${JobEnv})" ]; then kubectl create ns ${JobEnv}; fi

    kustomizeBuild /var/lib/jenkins/devops/${JobEnv}/${JobName}/
    kubectl get pod -n ${JobEnv} -o wide | grep ${deploymentName}
}