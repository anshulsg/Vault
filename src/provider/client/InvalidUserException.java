package provider.client;

class InvalidUserException extends Exception{
    private String exc_type;
    InvalidUserException(String type){
        exc_type=type;
    }
    public String toString(){
        return exc_type;
    }
}
