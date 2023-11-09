# Java-Socket-Programming HW 1

---

##Calculator Protocol

* 프로토콜 목적 :
  - 클라이언트 소켓과 서버소켓 간의 기본적인 사칙 연산에 대한 요청 및 응답 처리

* 프로토콜 장동 방식
    1. 서버 소켓 생성
    2. 클라이언트 소켓 생성되기 까지 서버 소켓 대기
    3. 클라이언트 소켓 생성
    4. 클라이언트 소켓 : 연산에 대한 요청 메시지 서버에 전송
    5. 서버 소켓 : 클라이언트로부터 메시지를 받아서 연산
    6. 서버 소켓 : 연산한 결과를 클라이언트에 전달
    7. 서버 소켓 : 연산 도중, 혹은 연산 전후에 에러 발생시 에러 메시지를 클라이언트에 전송
    8. 클라이언트 소켓 : "quit" 입력 시 프로토콜 중단
 
* 프로토콜 형식
  - [Operator] [Operand1] [Operand2] 순으로 입력
  - 연산자 종류
      - ADD : 덧셈 연산자
      - MIN : 뺄셈 연산자
      - MUL : 곱셈 연산자
      - DIV : 나눗셈 연산자
      - MOD : 나머지 연산자
   
  - Ex
      - ADD 10 20

  - 연산 결과 프로토콜 (서버 버전)
      - [Operand1] [Operator] [Operand2] = [Result]
      - Ex
          - 10.0 + 20.0 = 30.0
      - 단, 에러 발생시 에러 코드, 에러 메시지 출력
      - Ex
          - Error Code: [Error Code] Error Message: [Error Message]
       
            
            <img width="323" alt="스크린샷 2023-11-09 205016" src="https://github.com/james4510/Java-Socket-Programming/assets/110220397/15ee97f7-f219-4516-b1f4-a9dc60de14a6">

       
  - 연산결과 프로토콜 (클라이언트 버전)
      - Result: [Result]
      - Ex
          - Result: 30.0
      - 단, 에러 발생시 에러 코드, 에러 메시지 출력
      - Ex
          - Error Code: [Error Code] Error Message: [Error Message]

            <img width="267" alt="스크린샷 2023-11-09 204856" src="https://github.com/james4510/Java-Socket-Programming/assets/110220397/91f1ea00-c17c-490a-8495-821864bfcb59">

* 프로토콜 설계도
  ![설계도](https://github.com/james4510/Java-Socket-Programming/assets/110220397/32116347-6d2c-4e1e-b3cc-331ff912c2b3)


