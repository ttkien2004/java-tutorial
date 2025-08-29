# Java Spring Boot Tutorial Week 3

## Pagination + Search

- API: `GET /user-page/page?page=0&size=5`

- Trả về:

```json
{
  "content": [ { "id": 1, "name": "John", "email": "john@gmail.com" }, ... ],
  "page": 0,
  "size": 5,
  "totalElements": 20,
  "totalPages": 4
}

```

- JDBC: `SELECT \* FROM users LIMIT ? OFFSET ?`

- JPA: `Page<User> findAll(Pageable pageable)`

- `LIMIT`: Số lượng records muốn lấy (default là lấy từ đầu bảng).

- `OFFSET`: Có thể coi như index của bảng.

-> Khi kết hợp `LIMIT N` và `OFFSET I`, có nghĩa là tại index thứ I, lấy ra N
records.

## Validation + Exception Handler

### 1. Validation

- Khi tạo user `POST /users`:

  - `name` không được rỗng `@NotBlank`.
  - `email` đúng định dạng `@Email`.
  - `age` >= 18 `@Min(18).

- Nếu vi phạm, trả về lỗi JSON:

```json
{
	"status": 400,
	"errors": ["Name is required", "Email is invalid"]
}
```

### 2. Exception Handler

- Khi user không tồn tại, trả:

```json
{
	"status": 404,
	"message": "User not found"
}
```

- Sử dụng `@ControllerAdvice` + `@ExceptionHandler`.

### 3. Hiện thực

Tạo folder mới `dto` có chứa file `UserRequest.java`, sau đó import những
package sau:

```java
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
```

Sau đó, thêm những constraint của những field mà bạn muốn, ví dụ trường hợp này
là `email`, `name`, `age`:

```java
@NotBlank(message = "Name is required")
private String name;

@Email(message = "Email is invalid")
private String email;

@Min(value = 18, message = "Age must be >= 18")
private int age;
```

Trong `Controller`, thêm package `import jakarta.validation.Valid`, đồng thời
thêm trước parameter `@RequestBody` là `@Valid`

```java
public ResponseEntity<User> create(@Valid @RequestBody UserRequest userReq) {
    // Your code is here
}
```

Đối với `Exception Handler`, ta tạo folder `exception` và file
`GlobalExceptionHandler.java`:

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;
```

Định nghĩa các `exception`:

```java
// Bắt lỗi validation ver 1
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult()
                            .getFieldErrors()
                            .stream()
                            .map(err -> err.getDefaultMessage())
                            .collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ErrorResponse(400, errors)
    );
}

// bắt lỗi validation ver 2
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
    List<String> errors = new ArrayList<>();

    // Lặp từng lỗi field và thêm thông điệp vào list
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
        errors.add(fieldError.getDefaultMessage());
    }

    ErrorResponse response = new ErrorResponse(400, errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
}

// Custom Exception
@ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(404, ex.getMessage()));
}
```

Định nghĩa `ErrorResponse`:

```java
public static class ErrorResponse {
    private int status;
    private Object errors;

    public ErrorResponse(int status, Object errors) {
        this.status = status;
        this.errors = errors;
    }

    public int getStatus() { return status; }
    public Object getErrors() { return errors; }
}
```

Đối với `custom exception` như `UserNotFoundException` thì phải thêm tạo file
riêng cho class này:

```java
package com.example.demo.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

```

Sau khi định nghĩa các `exception` cần thiết, trong `service`, import các
exception này, và dùng `throw new Exception`.

## Spring Security + JWT

### 1. Đăng ký + Đăng nhập

- `POST /auth/register` → Lưu user + password hash (BCrypt).

- `POST /auth/login` → Nếu đúng mật khẩu → trả JWT token.

### 2. Bảo vệ API

- Chỉ `/auth/**` public.

- Các API `/users/**` yêu cầu gửi `Authorization: Bearer <token>` trong header.

### 3. JWT

- Sử dụng thư viện jjwt hoặc Spring Security built-in.

- Token chứa username và role.

### 4. Hiện thực

Thêm các properties này vào `application.properties`:

```bash
# Thêm secret và thời hạn token
app.jwt.secret=ChangeThisToASecretKeyAtLeast32CharsLong_1234567890
app.jwt.expiration-ms=3600000
```

Tạo các folder sau:

- `config`: `SecurityConfig`.

- `security`: `JwtUtil`, `JwtAuthenticationFilter`.

Trong folder `dto`, tạo class `RegisterRequest`, `LoginRequest`, `AuthResponse`:

- `RegisterRequest`, `LoginRequest`: Kiểm tra `input` như email, password, ...

- `AuthResponse`: Dùng để trả kết quả `token` khi người dùng đăng nhập thành
  công.

Sử dụng `@NotBlank`, `@Email`, `@Size` để kiểm tra các input.

Notes: Phải nhớ thêm các `getters/setters`.

Trong folder `security`, tạo file:

- `JwtUtil.java`

  - Nhiệm vụ: Sinh token, verify token.

  - Dủng thư viện `jjwt` hoặc `spring-jwt`.

  Ví dụ:

  ```java
  public String generateToken(UserDetails userDetails) {
  return Jwts.builder()
          .setSubject(userDetails.getUsername())
          .setIssuedAt(new Date())
          .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 ngày
          .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
          .compact();
  }
  ```

- `JwtAuthenticationFilter.java`

  - Đây là **filter custom** chạy trước khi request tới controller.

  - Nó lấy token từ header -> verify -> nếu `OK` thì tạo
    `UsernamePasswordAuthenticationToken` để Spring biết user nào đang login.

Trong folder `config` tạo file sau:

- `SecurityConfig.java`

  - Xác định route nào public `(/auth/**)`, route nào cần token (`/users/**`).

  - Nói cho Spring biết là phải dùng `JwtAuthenticationFilter`.

    Ví dụ:

    ```java
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**").permitAll()
                    .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    ```

Trong `AuthService`, ta thêm 2 attributes là `passwordEncoder` và `jwtUtil`:

```java
@Service
public class AuthService {
    private final AuthUserRepository authUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AuthUserRepository repo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authUserRepo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Your code is here
}

```

Các `method` có thể sử dụng của `passwordEncoder`:

- `encode(password)`: Dùng để tạo hash cho password.

- `matches(password_input, password_expected)`: Dùng để kiểm tra password nhập
  vào có đúng với expected không.

Các `method` có thể sử dụng của `jwtUtil`:

- `generateToken(arg1, arg2, ...)`: Hàm này có thể nhận tùy số lượng arguments
  (phụ thuộc vào dev).
