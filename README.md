# House Manager

DDD 2기 애정결핍덕후팀의 House Manager(가제)



## Technology Stack

- Kotlin
- Kotlin Coroutines
- AWS Lambda
- GraphQL



## Modules

### Shared

#### shared

여러 모듈에서 사용될 수 있는 공통된 것들을 모아놓은 모듈입니다.
현재는 데이터 모델과 Object <-> Primitives 변환 매커니즘이 있습니다.

#### buildSrc

전체적인 빌드 로직과 버전 정보를 관리하기 위한 Gradle의 특별한 모듈입니다. [문서 참조](https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources)

### Server

#### server:api

AWS Lambda와 상호작용하는 모듈입니다. 배포를 위한 템플릿과 관련 dependency를 포함하고 있습니다.
Handler 역시 이곳에서 작성합니다.

#### server:graphql

서버의 데이터 모델을 GraphQL의 Schema로 구현하는 모듈입니다.

#### server:data

MySQL DB와 상호작용하는 모듈입니다. 데이터 인출 및 갱신을 담당합니다.



## API Specification

### Few things to note

#### GraphQL

API의 유연성과 네트워크 부하 절약을 위해서 [GraphQL](<https://graphql.org/>)을 사용했습니다. 이에 따라 **하나의 Endpoint**를 통해서 대부분의 CRUD 작업을 처리할 수 있습니다. (인증 관련 API 제외)

서버에서는 GraphQL 구현을 위해서 코틀린과 함께 [KGraphQL](<https://github.com/pgutkowski/KGraphQL>) 라이브러리를 사용했습니다.

안드로이드, iOS 등 클라이언트에서는 GraphQL 클라이언트인 [apollo-android](<https://github.com/apollographql/apollo-android>), [apollo-ios](<https://github.com/apollographql/apollo-ios>)를 사용할 수 있습니다.

#### HTTPie

간결함을 위해서 예시에서는 [HTTPie](https://httpie.org)를 사용했습니다. HTTPie는 `curl` 을 대체할 수 있는 CLI 기반의 HTTP 클라이언트입니다.

#### Known Issues

왜 그런지는 모르겠지만 브라우저에서 GET Request를 날리면 400 Error를 반환하는 문제가 있는 것 같습니다. API 테스트를 할 때는 Postman같은 API 도구를 이용해주세요.



### Authentication

TBD



### Endpoints

#### GET /graphql

##### parameters

- query `required`

##### Examples

```bash
$ http <endpoint>/graphql?query={users{id}}
```

```json
Result:
{
  "data" : {
    "users" : [ {
      "id" : "1"
    }, {
      "id" : "2"
    }, {
      "id" : "3"
    } ]
  }
}
```

#### POST /graphql

##### parameters

- query `required`
- variables

##### Examples

```bash
$ http <endpoint>/graphql query:='{ users { id username }}'
```

```
Result:
{
  "data" : {
    "users" : [ {
      "id" : "1",
      "username" : "tura"
    }, {
      "id" : "2",
      "username" : "ddd"
    }, {
      "id" : "3",
      "username" : "alan"
    } ]
  }
}
```

#### GET /introspection/type

타입에 대한 정보를 가져옵니다.

##### parameters

- class `required`

##### examples

```
$ http <endpoint>/introspection/type?class=User
```

```
Result:
{
  "data" : {
    "__type" : {
      "name" : "User",
      "fields" : [ {
        "name" : "id",
        "type" : {
          "name" : null,
          "kind" : "NON_NULL",
          "ofType" : {
            "name" : "String",
            "kind" : "SCALAR"
          }
        }
      }, {
        "name" : "username",
        "type" : {
          "name" : null,
          "kind" : "NON_NULL",
          "ofType" : {
            "name" : "String",
            "kind" : "SCALAR"
          }
        }
      } ]
    }
  }
}
```



### Models

#### User

유저 정보를 나타내는 객체입니다.

#### Task

집안일을 나타내는 객체입니다.

