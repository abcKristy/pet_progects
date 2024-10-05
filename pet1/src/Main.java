import java.util.Scanner;


public class Main {

    public static int check(char[][] fild,int len,int wid){
        boolean fl=false;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < wid; j++) {
                if(fild[i][j]!='-'){
                    char sym = fild[i][j];
                    if(i<(wid-2)){
                        //->
                        if((sym==fild[i+1][j])&&(sym==fild[i+2][j])){
                            return 1;
                        }
                    }
                    if(j<(len-2)){
                        //down
                        if((sym==fild[i][j+1])&&(sym==fild[i][j+2])){
                            return 4;
                        }
                    }
                    if((i<=(wid-3))&&(j<=(len-3))){
                        //down-right
                        if((sym==fild[i+1][j+1])&&(sym==fild[i+2][j+2])){
                            return 5;
                        }
                    }
                    if((i>=2)&&(j<=(len-3))){
                        //down-left
                        if((sym==fild[i-1][j+1])&&(sym==fild[i-2][j+2])){
                            return 6;
                        }
                    }
                }else{
                    fl=true;
                }
            }
        }
        if(!fl){
            return 3;
        }
        return 2;
    }

    public static void out_fild(char[][] fild,int len,int wid){
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < wid; j++) {
                System.out.print(fild[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        Scanner inp = new Scanner(System.in);

        System.out.println("Игра Крестики-Нолики");
        System.out.print("Введите имя первого игрока ");
        String name_1player = inp.nextLine();
        System.out.print("Введите имя второго игрока ");
        String name_2player = inp.nextLine();
        System.out.println("Приветствуем "+name_1player+" и "+name_2player);
        System.out.println(name_1player+" будет играть за крестиков, а "+name_2player+" за ноликов.");

        int width=3,length=3;
        char[][] fild = new char[length][width];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                fild[i][j]='-';
            }
        }

        System.out.println("Доска для игры: ");
        out_fild(fild,length,width);

        boolean fin = false, player = true;

        while (!fin){
            if(player){
                System.out.println("Ход крестиков. Введите позицию");
            }
            else{
                System.out.println("Ход ноликов. Введите позицию");
            }

            System.out.print("Х:");
            int x = inp.nextInt();
            while((x>3)||(x<1)){
                System.out.println("Введите корректную позицию");
                x = inp.nextInt();
            }
            x-=1;

            System.out.print("Y:");
            int y = inp.nextInt();
            while((y>3)||(y<1)){
                System.out.println("Введите корректную позицию");
                y = inp.nextInt();
            }
            y-=1;

            if(player){
                fild[y][x]='x';
            }
            else{
                fild[y][x]='0';
            }

            System.out.println("Ход засчитан: ");
            out_fild(fild,length,width);

            if(check(fild,length,width)!=2){
                fin=true;
            }

            if(fin){
                System.out.print("Победил игрок за ");
                if(player){
                    System.out.println("крестики. Поздравляем "+name_1player);
                }
                else{
                    System.out.println("нолики. Поздравляем "+name_2player);
                }
            }

            player= !player;

        }
        System.out.println("Игра окончена! До новых встреч!");
    }
}