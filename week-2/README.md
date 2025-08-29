# Java Spring Boot tutorial Week 2

## Start Project

- Access [Create Spring Boot project](https://start.spring.io)

- Choose Project: Maven

- Language: Java

- Spring Boot: The latest version

- Project Metadata:

  - Group: `com.example`

  - Artifact: `demo`

- Packaging: Jar

- Java: your version

- Add Dependencies:
  - `Spring web`: Used for creating REST API.

## Run program

- Use command: `mvn spring-boot:run`

- Default host: `http://localhost:8080`

## Dependencies

- Method 1: On this website [Spring Boot Initializr](https://start.spring.io),
  Add dependencies that you want:

| Dependency                     | Function                                                           | When using it                               |
| ------------------------------ | ------------------------------------------------------------------ | ------------------------------------------- |
| spring-boot-starter-web        | Tạo API REST (HTTP) với @RestController, @GetMapping, @PostMapping | Mọi project web backend                     |
| spring-boot-starter-data-jpa   | Làm việc với database qua JPA/Hibernate                            | Khi cần CRUD với DB                         |
| mysql-connector-j              | Kết nối MySQL                                                      | Khi DB là MySQL                             |
| spring-boot-starter-validation | Validate dữ liệu input (@NotNull, @Email, …)                       | Khi cần kiểm tra dữ liệu người dùng gửi lên |
| spring-boot-starter-security   | Bảo mật, login, phân quyền                                         | Khi làm login, JWT, OAuth                   |
| spring-boot-starter-test       | Viết test (JUnit, Mockito)                                         | Khi muốn kiểm thử ứng dụng                  |

- Method 2: Add dependencies in `pom.xml` using this syntax:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>Name of dependency</artifactId>
</dependency>
```

- Then run `mvn clean install`

- If you want to skip Test part, run this command
  `mvn clean install -DskipTests`

## Java Syntax

### Connect to Database

Insert these properties into application.properties.

```java
spring.application.name=Java Spring Boot Tutorial

spring.datasource.url=jdbc:mysql://localhost:3306/your_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

```

### Repository

- **JDBC Template**

JDBC (Java Database Connectivity) là một API của Java để kết nối và làm việc với
cơ sở dữ liệu (MySQL, PostgreSQL, ...)

Import these packets:

```java
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
```

Khai báo JDBC Template:

```java
private final JdbcTemplate name;
```

- **RowMapper**

`RowMapper` là một `interface` trong Spring JDBC, dùng để chuyển từng dòng dữ
liệu bảng (ResultSet) thành một object Java.

```java
RowMapper<Object> = (rs, rowNum) -> {
    Object u;
    return u;
}
```

- **Các Methods**

Hầu hết các hàm đều xoay quanh cấu trúc này:

```java
String sql = "";
return jdbc.query(sql, rowMapper); // Nếu SELECT

return jdbc.update(sql, params...) // Nếu INSERT/ UPDATE/ DELETE
```

Giải pháp: `PreparedStatement` + `KeyHolder`

```java
KeyHolder keyHolder = new GeneratedKeyHolder();
jdbc.update(connection -> {
    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    ps.setString(1, user.getName()); // Gán giá trị vào thứ tự `?` trong SQL query.
    ps.setString(2, user.getEmail());
    return ps;
}, keyHolder);
```

- `Statement.RETURN_GENERATED_KEYS`: báo với MySQL sau khi `insert`, trả về khóa
  `id` được sinh ra.

- `KeyHolder`: giữ giá trị `id` trả về.

- `keyHolder.getKey()`: lấy `id`.

### Service

```java
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // Code của của method
}
```

### Controller

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("path")
    // Code các method của controller
    @PostMapping("path")
    // Code các method của controller
}

```

`ResponseEntity` trong Spring Boot là một lớp đại diện cho toàn bộ HTTP response
(bao gồm status code, headers và body).

Cấu trúc của `ResponseEntity`:

Một HttpResponse có 3 phần chính:

| Phần        | Ví dụ                                    | Giải thích          |
| ----------- | ---------------------------------------- | ------------------- |
| Status code | `200 OK`, `201 Created`, `404 Not found` | Kết quả của request |
| Headers     | `Content-Type: application/json`         | Metadata            |
| Body        | {"id": 1, "name": "A"}                   | Dữ liệu trả về      |

Ví dụ code:

```java
@PostMapping
public ResponseEntity<User> create(@RequestBody User user) {
  User created = service.create(user);
  return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
```

- `HttpStatus.CREATED` (201) -> báo cho client biết dữ liệu đã được tạo thành
  công.

- `body(created)` -> Trả về đối tượng `User` vừa được tạo.

```java
ResponseEntity.notFound().build();
ResponseEntity.ok(object);
```

- `ResponseEntity.notFound().build()`: Trả về `404 Not Found`.

- `ResponseEntity.ok(...)`: Trả `200 OK`.

- `@PathVariable var`: có thể hiểu là biến trên đường dẫn. Ví dụ:
  `http://example.com/1` -> Tìm bài báo hoặc file có id là 1.

## Tổng Kết Tuần 2

- Hiểu được cơ bản về Java Spring Boot

- Biết cách tạo project, thêm dependencies Spring Boot từ web
  [Create Spring Boot project](https://start.spring.io)

- Chia Cấu trúc thư mục thành `Model` -> `Repository` -> `Service` ->
  `Controller`.

- Kết nối với cơ sở dữ liệu MySQL, tạo các Controller cơ bản như: Thêm, Sửa,
  Đọc, Xóa.
