package model;

public class Carteira {
    private String codigo;
    private int saldo;
    private String strQRCode;


    public Carteira(String codigo, int saldo, String strQRCode) {
        this.codigo = codigo;
        this.saldo = saldo;
        this.strQRCode = strQRCode;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getStrQRCode() {
        return strQRCode;
    }

    public void setStrQRCode(String strQRCode) {
        this.strQRCode = strQRCode;
    }
}
