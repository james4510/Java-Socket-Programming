public class CalculatorProtocol {
    // 클라이언트에서 서버로 연산 요청을 보낼 때 사용하는 명령어
    public static final String ADD = "ADD";         //덧셈 연산자
    public static final String SUBTRACT = "MIN";    //뺄셈 연산자
    public static final String MULTIPLY = "MUL";    //곱셈 연산자
    public static final String DIVIDE = "DIV";      //나눗셈 연산자
    public static final String MODULAR = "MOD";     //나머지 연산자
    

    public static String Result(double result){
        return "Result: "+result;
    }

    

    //에러 메시지
    public static final String ERROR_INVALID_REQUEST = "Invalid request. Usage: [OPERATOR] [NUM1] [NUM2]";

    public static final String TOO_MANY_ARGS(){
        System.out.println("Incorrect: Too many arguments");
        throw new IllegalArgumentException("Too many arguments");
    }

    public static final String DIV_ZERO(){
        System.out.println("Incorrect: Division by zero");
        throw new ArithmeticException("Division by zero");
    }

    public static final String MOD_ZERO(){
        System.out.println("Incorrect: Division by zero");
        throw new ArithmeticException("Division by zero");
    }

    public static final String INVALID_OPERATOR(){
        System.out.println("Incorrect: Invalid operator");
        throw new IllegalArgumentException("Invalid operator"); 
    }
    //연산자 목록
    public static final String[] VALID_OPERATORS = {ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULAR};

}
