import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerEx{

    

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
        String fileName = "Server_Info.dat";
        writeDataToFile(fileName);
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String hostName = bufferedReader.readLine();
        int portNumber = Integer.parseInt(bufferedReader.readLine());

        bufferedReader.close();
        fileReader.close();

        
        ServerSocket listener = new ServerSocket(portNumber);
        System.out.println("The calculator server is running...");

        int numThreads = 10; // 원하는 최대 스레드 수로 조정
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        while (true) {
            Socket socket = listener.accept();
            System.out.println("Connected: " + socket);

            // 클라이언트 요청을 처리할 스레드를 풀에 제출
            executor.submit(new Calculator(socket));
        }
    }

    private static class Calculator implements Runnable {
        private Socket socket;

        Calculator(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                

                while (in.hasNextLine()) {
                    String input = in.nextLine();
                    
                    if (input.equalsIgnoreCase("quit")) {
                        break;
                    }

                    try {
                        double result = evaluateExpression(input);
                        out.println(result);
                    } catch (Exception e) {
                        out.println("Error: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                    System.out.println("Closed: " + socket);
                } catch (IOException e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }
        private double evaluateExpression(String expression) {

            StringTokenizer tokenizer = new StringTokenizer(expression, " ");
            String operator = tokenizer.nextToken();
            double operand1 = Double.parseDouble(tokenizer.nextToken());
            double operand2 = Double.parseDouble(tokenizer.nextToken());
            
            if(tokenizer.hasMoreTokens()){
                CalculatorProtocol.TOO_MANY_ARGS();
            }

            double  result = 0.0;
            switch (operator) {
                case CalculatorProtocol.ADD:{
                    System.out.println(operand1+" + "+operand2+" = "+(operand1+operand2));
                    result = operand1 + operand2;
                    return result;
                }
                case CalculatorProtocol.SUBTRACT:{
                    System.out.println(operand1+" - "+operand2+" = "+(operand1-operand2));
                    result =  operand1 - operand2;
                    return result;
                }   
                case CalculatorProtocol.MULTIPLY:{
                    System.out.println(operand1+" X "+operand2+" = "+(operand1*operand2));
                    result =  operand1 * operand2;
                    return result;
                }   
                case CalculatorProtocol.DIVIDE:
                    if (operand2 != 0) {
                        System.out.println(operand1+" / "+operand2+" = "+(operand1/operand2));
                        result =  Math.floor(operand1 / operand2);
                        return result;
                    } else 
                        CalculatorProtocol.DIV_ZERO();
                case CalculatorProtocol.MODULAR:
                    if (operand2 != 0) {
                        System.out.println(operand1+" % "+operand2+" = "+(operand1%operand2));
                        result =  operand1 % operand2;
                        return result;
                    } 
                    else
                        CalculatorProtocol.MOD_ZERO(); 
                default:
                    CalculatorProtocol.INVALID_OPERATOR();
            }
            return result;

            
        }
        
    }
}