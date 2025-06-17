package GiostriConstrucoes.dev.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public interface IDataUtil {

    default Date toData(String d) {
        try {
            String x = d.substring(0, 10);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(x);
        }catch (Exception e) {
            return null;
        }
    }

    default String dataToString(Date data) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(data);
    }

    default LocalDate toLocalDate(Date data) {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    default LocalDate toLocalDate(String data) {
        return toData(data).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
