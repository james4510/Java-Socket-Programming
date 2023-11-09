import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerEx{

    
    //맨 처음 서버를 구동할 때 파잉ㄹ에 대한 정보 생성하는 매서드
    public static void writeDataToFile(String fileName){
        try{
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write("localhost");
            bufferedWriter.newLine();
            bufferedWriter.write(Integer.toString(5001));
            bufferedWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        //서버 정보 파일 읽어들여서 서버IP, 포트 번호에 대한 정보 가져오기
        String fileName = "Server_Info.dat";
        writeDataToFile(fileName);
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String hostName = bufferedReader.readLine();
        int portNumber = Integer.parseInt(bufferedReader.readLine());

        bufferedReader.close();
        fileReader.close();

        
        //메인서버 소켓 생성
        ServerSocket listener = new ServerSocket(portNumber);
        System.out.println("The calculator server is running...");

        
        int numThreads = 10; // 원하는 최대 스레드 수로 조정
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        while (true) {
            //클라이언트와 데이터를 주고받는 소켓 생성
            Socket socket = listener.accept();
            System.out.println("Connected: " + socket);

            // 클라이언트 요청을 처리할 스레드를 풀에 전달
            executor.submit(new Calculator(socket));
        }
    }

    //실제 연산을 하는 계산기 클래스
    private static class Calculator implements Runnable {
        private Socket socket;  //클라이언트와 데이터를 주고 받는 임시 소켓

        Calculator(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                //클라이언트로부터 받은 데이터를 읽어들인다.
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                

                while (in.hasNextLine()) {
                    String input = in.nextLine();
                    
                    //"quit" 입력 시 종료
                    if (input.equalsIgnoreCase("quit")) {
                        break;
                    }

                    try {
                        //클라이언트와 서버에 연산 결과에 대한 정보 전송
                        String result = evaluateExpression(input);  
                        out.println(result);
                    } catch (Exception e) {
                        out.println("Error: " + e.getMessage());    //출력하는데 에러 발생시 에러 메세지 출력
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());     //이 과정에서 에러 발생시 에러 메세지 출력
            } finally {
                try {
                    //서버 소켓 종료
                    socket.close();
                    System.out.println("Closed: " + socket);
                } catch (IOException e) {
                    System.out.println("Error closing socket: " + e.getMessage());  //소켓을 닫는데 에러 발생시 에러 메세지 출력
                }
            }
        }

        //에러 발생시 에러에 대한 정보 출력하는 매서드
        private static String errorMessage(String errorCode, String errorMsg){
            System.out.println("Error Code: "+errorCode+" Error Message: "+errorMsg);
            return "Error Code: "+errorCode+" Error Message: "+errorMsg;
        }

        //연산을 하는 매서드
        private String evaluateExpression(String expression) {

            //띄어쓰기를 구분자로 하여 토큰 단위로 분리
            StringTokenizer tokenizer = new StringTokenizer(expression, " ");
            String operator = tokenizer.nextToken();
            double operand1 = Double.parseDouble(tokenizer.nextToken());
            double operand2 = Double.parseDouble(tokenizer.nextToken());
            
            //만약 두번째 피연산자 이후 토큰이 더 존재할 경우 에러 메세지 출력
            if(tokenizer.hasMoreTokens()){
                return errorMessage("400", "Too many arguments");
            }

            double result = 0.0;
            switch (operator) {
                case "ADD":{
                    System.out.println(operand1+" + "+operand2+" = "+(operand1+operand2));
                    result = operand1 + operand2;
                    return "Result: "+result;
                }
                case "MIN":{
                    System.out.println(operand1+" - "+operand2+" = "+(operand1-operand2));
                    result =  operand1 - operand2;
                    return "Result: "+result;
                }   
                case "MUL":{
                    System.out.println(operand1+" X "+operand2+" = "+(operand1*operand2));
                    result =  operand1 * operand2;
                    return "Result: "+result;
                }   
                case "DIV":
                    if (operand2 != 0) {
                        System.out.println(operand1+" / "+operand2+" = "+(operand1/operand2));
                        result =  Math.floor(operand1 / operand2);
                        return "Result: "+result;
                    } else 
                        return errorMessage("401", "Division by zero");
                case "MOD":
                    if (operand2 != 0) {
                        System.out.println(operand1+" % "+operand2+" = "+(operand1%operand2));
                        result =  operand1 % operand2;
                        return "Result: "+result;
                    } 
                    else
                        return errorMessage("401", "Division by zero");
                default:
                    return errorMessage("402", "Invalid Operator");
            }
            
        }
        
    }
}
