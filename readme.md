# 사원관리 프로그램

> JDBC를 이용해 오라클 DB에 접속해서 사원 데이터를 관리하는 프로그램



## DB

- 테이블 EMPLOYEE, DEPARTMENT, JOB을 이용
- 🚨EMPLOYEE의 일부 속성은 DEPARTMENT와 JOB의 기본키를 참조하고 있으므로, 이를 JAVA에서 객체지향적으로 표현



## 기능목록

### 1. 전체 사원 조회

### 2. 사원 조회

- 부서
- 직책
- 이름
- 급여 (급여 입력 시 <code>이상</code>, <code>이하</code> 설정)

### 3. 사원 등록

### 4. 사원 수정

- 직책, 부서, 급여, 전화번호, 이메일 수정

### 5. 사원 삭제

### 6. 부서 관리

- 등록
- 수정
- 삭제

### 7. 직책 관리

- 등록
- 수정
- 삭제