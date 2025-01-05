import json

import pytest
import time
import sys
import requests

headers = {
    'Content-Type': 'application/json',
    'Host': '127.0.0.1:9009',
    'Accept': 'application/json'
}


@pytest.mark.run('first')
def test_query_dept():
    for i in range(10000):
        req = requests.get('http://127.0.0.1:9009/management/manage/hos/dept/list?page=0&size=100',
                           headers=headers)
        data = json.loads(req.text)
        if data['code'] != 0:
            sys.exit(0)


@pytest.mark.run('second')
def test_add_dept():
    dept = {
        "deptCode": "1",
        "deptName": "deptName",
        "state": 1
    }
    err_cnt = 1
    normal_cnt = 1
    for i in range(10000):
        dept['deptCode'] = str(i+1)
        req = requests.post('http://127.0.0.1:9009/management/manage/hos/dept/add',
                           headers=headers,
                           data=json.dumps(dept))
        data = json.loads(req.text)
        if data['code'] == -1:
            err_cnt += 1
        elif data['code'] == 0:
            normal_cnt += 1
        else:
            sys.exit(0)
    print(normal_cnt, err_cnt)


@pytest.mark.run('third')
def test_update_dept():
    dept = {
        "deptCode": "1",
        "deptName": "deptName",
        "state": 1
    }
    err_cnt = 1
    normal_cnt = 1
    for i in range(10000):
        dept['deptName'] = 'deptName' + str(i)
        req = requests.post('http://127.0.0.1:9009/management/manage/hos/dept/update',
                           headers=headers,
                           data=json.dumps(dept))
        data = json.loads(req.text)
        if data['code'] == -1:
            err_cnt += 1
        elif data['code'] == 0:
            normal_cnt += 1
        else:
            sys.exit(0)
    print(normal_cnt, err_cnt)


@pytest.mark.run('forth')
def test_delete_dept():
    err_cnt = 1
    normal_cnt = 1
    for i in range(10000):
        dept_code = str(i+1)
        req = requests.delete('http://127.0.0.1:9009/management/manage/hos/dept/delete?deptCode=' + str(dept_code),
                           headers=headers)
        data = json.loads(req.text)
        if data['code'] == -1:
            err_cnt += 1
        elif data['code'] == 0:
            normal_cnt += 1
        else:
            sys.exit(0)
    print(normal_cnt, err_cnt)
