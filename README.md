# House Manager API

DDD 2기 애정결핍덕후팀의 House Manager(가제) API



## Technology Stack

- Kotlin
- Kotlin Coroutines
- AWS Lambda
- GraphQL



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



### TODO

앞으로 할 일을 우선순위가 높은 순서대로 나열합니다.

- [ ] RDS 연결, 실제 Database

- [ ] JWT Authentication