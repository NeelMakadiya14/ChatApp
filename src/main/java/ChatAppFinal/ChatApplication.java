package ChatAppFinal;

public abstract class ChatApplication {

    private String userName;
    private Helper helper = new Helper();

    public Helper getHelper() {
        return helper;
    }

    public void setHelper(Helper helper) {
        this.helper = helper;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUp(){

        userName = helper.getUserInput("Enter The UserName : ");

        Thread listener = new Thread(new Runnable() {
            @Override
            public void run() {
                receive();
            }
        });

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    send();
                }

            }
        });

        listener.start();
        sender.start();
    }

    public abstract void send();

    public abstract void receive();

}
