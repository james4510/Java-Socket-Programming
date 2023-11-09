import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientEx {
    public static void main(String[] args) throws Exception {
        
        //서버 정보 파일 읽어들여서 서버IP, 포트 번호에 대한 정보 가져오기
        String fileName = "Server_Info.dat";
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String hostName = bufferedReader.readLine();
        int portNumber = Integer.parseInt(bufferedReader.readLine());

        bufferedReader.close();
        fileReader.close();

        //서버 정보 파일에서 가져온 서버 정보로 새로운 클라이언트 소켓 생성
        Socket socket = new Socket(hostName, portNumber);
        System.out.println("Enter a mathematical expression (e.g., ADD 10 20) or 'quit' to exit");

        //사용자로부터 데이터 입력받기
        Scanner scanner = new Scanner(System.in);
        Scanner in = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        //서버 소켓에 사용자로 부터 받은 데이터 전송 및 결과 가져오기
        while (true) {
            System.out.print("Expression: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("quit")) {
                out.println("quit");
                break;
            }

            out.println(input);
            String result = in.nextLine();
            System.out.println(result);
        }
        
        socket.close();
    }
}
