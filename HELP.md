# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.0/maven-plugin/reference/html/#build-image)

# 根据 username 获得userId : 
# https://gitlab.gz.cvte.cn/api/v4/users?username=i_linjunyi
# https:// gitlab地址 /api/v4/users?username= 用户名

# 创建MR : post   https://gitlab.gz.cvte.cn/api/v4/projects/1602%2Fbe%2Fzeus/merge_requests
# Body: 
#  {
#      "id" :  "1602%2Fbe%2Fzeus",
#      "source_branch" :  "test_commit",
#      "target_branch" : "feature/addComitInfo",
#      "title" : "test111",
#      "assignee_id" : "0"
#  }
# Headers: 
#  PRIVATE-TOKEN : xxx
  
  
# 比较分支 :
# https://gitlab.gz.cvte.cn/api/v4/projects/1602%2Fbe%2Fzeus/repository/compare?from=test_commit&to=feature%2FaddComitInfo
# Headers: 
#  PRIVATE-TOKEN : xxx