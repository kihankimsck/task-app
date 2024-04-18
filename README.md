## Spring Initializr ADD DEPENDENCIES
- Spring Boot DevTools
- Lombok
- Spring Web
- Spring Data JPA
- H2 Database
- Validation
- thymeleaf
- Mybatis

---
## application.properties (JPA)

```properties
# application name
spring.application.name=todo-app

# server port
server.port=8080

# h2 database
spring.datasource.url=jdbc:h2:file:./data/task
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true

# h2 database console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.trace=false
spring.h2.console.settings.web-allow-others=false

# h2 database table
spring.jpa.hibernate.ddl-auto=update

# devtools
spring.devtools.restart.enabled=true

# thymeleaf
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false

```

---

## Spring Boot default Classpath
* classpath:/META-INF/resources/
* classpath:/resources/
* classpath:/static/
* classpath:/public/

---

## Web Configuration

```java
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/templates/", "classpath:/static/")
            .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
    }
}
```

———

## bootstrap & jquery webjar
```xml
<!-- https://mvnrepository.com/artifact/org.webjars/bootstrap -->
<dependency>
	<groupId>org.webjars</groupId>
	<artifactId>bootstrap</artifactId>
	<version>5.3.3</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.webjars/jquery -->
<dependency>
	<groupId>org.webjars</groupId>
	<artifactId>jquery</artifactId>
	<version>3.7.1</version>
</dependency>
```

———

# index.html

```html
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" th:href="@{/webjars/bootstrap/5.3.3/css/bootstrap.min.css}">
    <title>index</title>
</head>
<body>
    index
    <script th:src="@{/webjars/jquery/3.7.1/jquery.min.js}"></script>
    <script th:src="@{/webjars/bootstrap/5.3.3/js/bootstrap.min.js}"></script>
</body>
</html>
```

```html

<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="stylesheet" th:href="@{/webjars/bootstrap/5.3.3/css/bootstrap.min.css}">
    <title>index</title>
</head>
<body>
<div class="container">
    <h1 class="text-container">태스크 목록</h1>
    <br/>
    <p th:text="${today}">Today</p>
    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th>아이디</th>
            <th>할일</th>
            <th>완료</th>
            <th>생성일시</th>
            <th>수정일시</th>
            <th>변경</th>
        </tr>
        </thead>
        <tbody>
        <tr th:each="item : ${tasks}" th:class="${item.complete} ? success : warning">
            <td th:text="${item.id}">item_id</td>
            <td th:text="${item.description}"></td>
            <td th:text="${item.complete} == true ? '완료' : '진행중'"></td>
            <td th:text="${#temporals.format(item.createdAt, 'yyyy년 MM월 dd일 HH시 mm분')}"></td>
            <td th:text="${#temporals.format(item.updatedAt, 'yyyy년 MM월 dd일 HH시 mm분')}"></td>
            <td>
                <div class="btn btn-group-sm" role="group">
                    <a class="btn btn-info" th:href="@{/moveToUpdate/{id}(id=${item.id})}">수정</a>
                    <a class="btn btn-danger" th:href="@{/delete/{id}(id=${item.id})}">삭제</a>
                </div>
            </td>
        </tr>
        </tbody>
    </table>

    <p><a class="btn btn-success" th:href="@{/moveToCreate}">태스크 추가</a></p>

</div>
<script th:src="@{/webjars/jquery/3.7.1/jquery.min.js}"></script>
<script th:src="@{/webjars/bootstrap/5.3.3/js/bootstrap.min.js}"></script>
</body>
</html>

```

———

## Controller

```java 
import com.example.todoapp.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Controller
public class HomeController {

    @Autowired
    TaskService taskService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", taskService.getTasks());
        model.addAttribute("today", LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime().getDayOfWeek());
        return "index";
    }
}


```

```java

package com.example.todoapp.controllers;

import com.example.todoapp.models.Task;
import com.example.todoapp.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Controller
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping("/moveToUpdate/{id}")
    public String moveToUpdateForm(@PathVariable("id") long id, Model model) {
        Task task = taskService.getTask(id);
        model.addAttribute("task", task);
        return "updateForm";
    }

    @GetMapping("/moveToCreate")
    public String moveToCreate(Task task) {
        return "createForm";
    }

    @PostMapping("/create")
    public String create(@Valid Task task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "createForm";
        }

        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        // Mybatis
        taskService.insertTask(task);

        return "redirect:/";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Task task, BindingResult bindingResult, Model model) {
        //
        if (bindingResult.hasErrors()) {
            task.setId(id);
            return "updateForm";
        }
        //
        task.setUpdatedAt(LocalDateTime.now());

        // Mybatis
        taskService.updateTask(task);

        //
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {

        //
        Optional<Task> task = Optional.ofNullable(taskService.getTask(id));
        if (task.isPresent()) {
            taskService.deleteTask(task.get());
        } else {
            throw new IllegalArgumentException("존재하지 않는 태스크 입니다.");
        }

        return "redirect:/";
    }

    @GetMapping("/{age}")
    public String combinedurl(@PathVariable("age") int age, @RequestParam("name") String name) {
        log.info(String.format("age -> %d, name : %s", age, name));
        return "redirect:/";
    }
}


```

———

## Model

```java
@Data
public class Task {
    private Long id;
    private String description;
    private boolean complete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

```

———
# create.html

```html

<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="stylesheet" th:href="@{/webjars/bootstrap/5.3.3/css/bootstrap.min.css}">
    <title>등록하기</title>
</head>
<body>
<div class="container">
    <h1 class="text-container">등록하기</h1>
    <br/>

    <div class="col-md-6">
        <form action="#"
              th:action="@{/create}"
              th:object="${task}"
              method="post">
            <div class="form-group">
                <label for="description">할일</label>
                <input class="form-control" type="text" th:field="*{description}" placeholder="할일을 입력하세요." id="description" />
                <span th:if="${#fields.hasErrors('description')}" th:errors="*{description}"></span>
            </div>
            <button class="btn btn-primary" type="submit">등록하기</button>
        </form>
    </div>

</div>
<script th:src="@{/webjars/jquery/3.7.1/jquery.min.js}"></script>
<script th:src="@{/webjars/bootstrap/5.3.3/js/bootstrap.min.js}"></script>
</body>
</html>

```

---
# update.html

```html

<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="stylesheet" th:href="@{/webjars/bootstrap/5.3.3/css/bootstrap.min.css}">
    <title>수정하기</title>
</head>
<body>
<div class="container">
    <h1 class="text-container">수정하기</h1>
    <br/>

    <div class="col-md-6">
        <form action="#"
              th:action="@{/update/{id}(id=${task.id})}"
              th:object="${task}"
              method="post">
            <div class="form-group">
                <label for="description">할일</label>
                <input class="form-control" type="text" th:field="*{description}" placeholder="할일을 입력하세요." id="description" />
                <span th:if="${#fields.hasErrors('description')}" th:errors="*{description}"></span>
            </div>
            <div class="checkbox">
                <label>
                    <input type="checkbox" th:checked="${complete} == true" th:field="*{complete}" id="complete" />
                    완료
                </label>
            </div>

            <button class="btn btn-primary" type="submit">수정하기</button>
            <input type="hidden" th:field="*{createdAt}" />
            <input type="hidden" th:field="*{updatedAt}" />
        </form>
    </div>

</div>
<script th:src="@{/webjars/jquery/3.7.1/jquery.min.js}"></script>
<script th:src="@{/webjars/bootstrap/5.3.3/js/bootstrap.min.js}"></script>
</body>
</html>

```

---
## application.properties (Mapper)

```properties
# application name
spring.application.name=task-app

# server port
server.port=8080

# mariadb
spring.datasource.driverClassName=net.sf.log4jdbc.sql.jdbcapi.DriverSpy
spring.datasource.username=id
spring.datasource.password=password
spring.datasource.url=jdbc:log4jdbc:mariadb://ip:post/spring?allowMultiQueries=true

# devtools
spring.devtools.restart.enabled=true

# thymeleaf
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false

# Mapper
mybatis.mapper-locations=mapper/**/*.xml
logging.level.jdbc.sqlonly=off
logging.level.jdbc.sqltiming=info
logging.level.jdbc.resultsettable=info
logging.level.jdbc.audit=off
logging.level.jdbc.resultset=off
logging.level.jdbc.connection=off
```

---

## Service
```java


import com.example.todoapp.dao.TaskDao;
import com.example.todoapp.models.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskService {

    @Autowired
    TaskDao taskDao;

    public List<Task> getTasks() {
        return taskDao.getTasks();
    }

    public void insertTask(Task task) {
        taskDao.insertTask(task);
    }

    public void updateTask(Task task) {
        taskDao.updateTask(task);
    }

    public void deleteTask(Task task) {
        taskDao.deleteTask(task);
    }
}

```


---

## Dao
```java

import com.example.todoapp.models.Task;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TaskDao {
    List<Task> getTasks();

    int insertTask(Task task);

    void updateTask(Task task);

    void deleteTask(Task task);

}

```

---

## Mapper.xml

```xml

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.todoapp.dao.TaskDao">

    <select id="getTasks" resultType="com.example.todoapp.models.Task">
        select id, description, complete, createdAt, updatedAt from task
    </select>

    <select id="getTask" resultType="com.example.todoapp.models.Task">
        select * from task where id = #{id}
    </select>

    <insert id="insertTask" parameterType="com.example.todoapp.models.Task">
        insert into task (
        description, complete, createdAt, updatedAt
        ) values (
        #{description}, #{complete}, #{createdAt}, #{updatedAt}
        )
    </insert>

    <update id="updateTask" parameterType="com.example.todoapp.models.Task">
        update
        task
        set
        description = #{description}, complete = #{complete}
        where
        id = #{id}
    </update>

    <delete id="deleteTask" parameterType="com.example.todoapp.models.Task">
        delete from task where id = #{id}
    </delete>
</mapper>

```

---
## pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.4</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>todo-app</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>todo-app</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>3.0.3</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter-test</artifactId>
            <version>3.0.3</version>
            <scope>test</scope>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.webjars/bootstrap -->
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>bootstrap</artifactId>
            <version>5.3.3</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.webjars/jquery -->
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>jquery</artifactId>
            <version>3.7.1</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.bgee.log4jdbc-log4j2/log4jdbc-log4j2-jdbc4.1 -->
        <dependency>
            <groupId>org.bgee.log4jdbc-log4j2</groupId>
            <artifactId>log4jdbc-log4j2-jdbc4.1</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.mariadb.jdbc</groupId>
            <artifactId>mariadb-java-client</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!--		&lt;!&ndash; https://mvnrepository.com/artifact/com.oracle.database.jdbc/ojdbc8 &ndash;&gt;-->
        <!--		<dependency>-->
        <!--			<groupId>com.oracle.database.jdbc</groupId>-->
        <!--			<artifactId>ojdbc8</artifactId>-->
        <!--			<version>23.3.0.23.09</version>-->
        <!--		</dependency>-->
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

```
---
## MySQL 
- DB : spring
- table : task

```sql

        CREATE TABLE `spring`.`task` (
        `id` INT NOT NULL AUTO_INCREMENT COMMENT '태스크 아이디\n',
        `description` VARCHAR(45) NOT NULL COMMENT '태스크 내용',
        `complete` TINYINT NOT NULL COMMENT '태스크 완료 여부',
        `createdAt` DATETIME NOT NULL COMMENT '등록일시',
        `updatedAt` DATETIME NOT NULL COMMENT '수정일시\n',
        PRIMARY KEY (`id`));
```

---

## git
https://github.com/kihankimsck/springboot-task-app
```shell
git remote add origin git@github.com:kihankimsck/task-app.git
git branch -M main
git push -u origin main
```
