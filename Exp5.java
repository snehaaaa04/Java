import java.util.Scanner;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Integer>  numbers = new ArrayList<>();
        System.out.println("Enter integers(type 'done' to finish):");
        while(true){
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("done")){
                break;
            }try{
                int num = Integer.parseInt(input);
                numbers.add(num);
            }catch(NumberFormatException e){
                System.out.println("Invalid input! Please enter an integer or 'done' to finish.");
            }
        }
        int sum = 0;
        for(Integer num : numbers){
             sum += num;
        }

        System.out.println("Numbers entered: " + numbers);
        System.out.println("Sum of integers: " + sum);

        sc.close();
    }
}
