package com.traduzzo.traduzzoApi.enumeracoes;

public enum Estado {
    AC, AL, AP, AM, BA, CE, DF, ES, GO, MA, MT, MS, MG, PA, PB, PR, PE, PI, RJ, RN, RS, RO, RR, SC, SP, SE, TO;

    public static boolean estadoValido(String estado) {
        if (estado == null) return false;
        try {
            Estado.valueOf(estado.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
