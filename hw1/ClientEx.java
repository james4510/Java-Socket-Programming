import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientEx {
    public static void main(String[] args) throws Exception {
       
        String fileName = "Server_Info.dat";
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String hostName = bufferedReader.readLine();
        int portNumber = Integer.parseInt(bufferedReader.readLine());

        bufferedReader.close();
        fileReader.close();

        Socket socket = new Socket(hostName, portNumber);
        System.out.println("Enter a mathematical expression (e.g., ADD 10 20) or 'quit' to exit");

        Scanner scanner = new Scanner(System.in);
        Scanner in = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

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
