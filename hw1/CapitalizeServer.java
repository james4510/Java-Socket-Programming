import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CapitalizeServer {
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
                        out.println(Double.toString(result));
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
            // 간단한 수식 평가 로직을 구현하세요.
            // 이 예제에서는 수식을 파싱하여 연산자와 피연산자를 분리하고 계산합니다.
            // 실제로 더 복잡한 계산 로직을 구현할 수 있습니다.
            String[] parts = expression.split(" ");
            double operand1 = Double.parseDouble(parts[1]);
            String operator = parts[0];
            double operand2 = Double.parseDouble(parts[2]);


            switch (operator) {
                case "ADD":
                    return operand1 + operand2;
                case "MIN":
                    return operand1 - operand2;
                case "MUL":
                    return operand1 * operand2;
                case "DIV":
                    if (operand2 != 0) {
                        return operand1 / operand2;
                    } else {
                        throw new ArithmeticException("Incorrect: \nDivision by zero");
                    }
                default:
                    throw new IllegalArgumentException("Invalid operator: " + operator);
            }
        }
    }
}
