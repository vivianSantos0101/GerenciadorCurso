package gui.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern CPF_PATTERN = Pattern.compile(
        "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$"
    );
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidCPF(String cpf) {
        if (cpf == null || !CPF_PATTERN.matcher(cpf).matches()) {
            return false;
        }
        
        // Remove ponto e traço
        String cleanCPF = cpf.replaceAll("[.-]", "");
        
        // ve se nao tem digito igual
        
        if (cleanCPF.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // ValidaÇÃO de CPF REAIS//
        try {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Integer.parseInt(String.valueOf(cleanCPF.charAt(i))) * (10 - i);
            }
            int remainder = sum % 11;
            int digit1 = remainder < 2 ? 0 : 11 - remainder;
            
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Integer.parseInt(String.valueOf(cleanCPF.charAt(i))) * (11 - i);
            }
            remainder = sum % 11;
            int digit2 = remainder < 2 ? 0 : 11 - remainder;
            
            return digit1 == Integer.parseInt(String.valueOf(cleanCPF.charAt(9))) &&
                   digit2 == Integer.parseInt(String.valueOf(cleanCPF.charAt(10)));
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    
    // VALIDAR IDADE
    public static boolean isValidAge(LocalDate birthDate) {
        if (birthDate == null) {
            return false;
        }
        
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);
        
        return period.getYears() >= 16 && period.getYears() <= 100; // validação de idade pra ver se nao é nenem nem vampiro
    }
    
    
    // VALIDAR NOME 
    
    public static boolean isValidName(String name) {
        return name != null && name.trim().length() >= 3 && name.trim().length() <= 100; 
    }
    
    // NOME DE CURSO VALIDO
    
    public static boolean isValidCourseName(String name) {
        return name != null && name.trim().length() >= 3 && name.trim().length() <= 50; 
    }
    
    // CARGA HORARIA VALIDA
    
    public static boolean isValidWorkload(int workload) { 
        return workload >= 20 && workload <= 1000;
    }
    
    //LIMITE DE ESTUDANTE
    public static boolean isValidStudentLimit(int limit) { 
        return limit >= 1 && limit <= 500;
    }
    
    //FORMATAÇÃO DE CPF
    
    public static String formatCPF(String cpf) {
        if (cpf == null) return "";
        
        // tira o que n é numero
        String cleanCPF = cpf.replaceAll("\\D", "");
        
        if (cleanCPF.length() != 11) {
            return cpf; 
        }
        
        // Formatação com ponto e traço  XXX.XXX.XXX-XX
        return cleanCPF.substring(0, 3) + "." + 
               cleanCPF.substring(3, 6) + "." + 
               cleanCPF.substring(6, 9) + "-" + 
               cleanCPF.substring(9, 11);
    }
} 