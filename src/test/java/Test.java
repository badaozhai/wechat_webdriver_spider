
 public class Test
 {
    public static void main(String args[]){

            Hello h = new Hello();
            Hello h2 = new Hello();

    }
}

 class Hello{

     public Hello(){
         System.out.println("Test构造函数执行");
     }
     static{
         System.out.println("static语句块执行");
     }

 }