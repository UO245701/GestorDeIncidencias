package util;

public class IncidenciaValidator {

    public static void validateCampos(String usuario, String tipo, String descripcion, String localizacion) {
        if (isBlank(usuario)) {
            throw new IllegalArgumentException("Debes introducir tu correo o DNI.");
        }
        if (isBlank(tipo)) {
            throw new IllegalArgumentException("Debes seleccionar el tipo de incidencia.");
        }
        if (isBlank(descripcion)) {
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }
        if (isBlank(localizacion)) {
            throw new IllegalArgumentException("La localización es obligatoria.");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}