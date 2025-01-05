
package scala.cn.swifthealth.manage.hosinfo

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DeptInfoApi extends Simulation {
  val httpProtocol = http
   .baseUrl("http://localhost:9009") // Spring Boot应用的地址
   .acceptHeader("application/json")

  val scn = scenario("DeptInfo Api")
   .exec(http("Get DeptInfo")
     .get("/manage/hos/dept/list?page=0&size=10"))

  setUp(scn.inject(atOnceUsers(100)).protocols(httpProtocol))
}
