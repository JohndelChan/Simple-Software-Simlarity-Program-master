import java.io.*;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        File file = new File("C:\\Users\\11838000\\Desktop\\Codes\\Test 3.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        String code1 = "";
        String code2 = "";
        while((st=br.readLine())!=null)
        {
            System.out.println(st);
            code1=code1+st;
        }



        file = new File("C:\\Users\\11838000\\Desktop\\Codes\\Test 4.txt");
        br = new BufferedReader(new FileReader(file));
        while((st=br.readLine())!=null)
        {
            System.out.println(st);
            code2=code2+st;
        }

        code1=code1.replaceAll("\\s","");
        code2=code2.replaceAll("\\s","");

        System.out.println("CODE 1:"+code1);
        System.out.println("CODE 2:"+code2);

        CompareCode comp = new CompareCode();

        comp.compare(code1,code2);

    }
}

public class CompareCode {
 private String string1;
 private String string2;
 private int pointer;

 public void compare(String str1, String str2)
 {
    for(int i=0;i<str1.length();i++)
    {
        if(i>=str2.length()){
            pointer++;
        }
        else if(str1.charAt(i)==str2.charAt(i))
        {
            pointer--;
        }
        else pointer++;
    }

    System.out.println(pointer);

    if(pointer<0)
    {
        System.out.println("Same Code");
    }
    else
    {
        System.out.println("Different Code");
    }
 }
}

