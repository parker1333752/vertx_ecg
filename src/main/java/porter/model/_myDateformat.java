package porter.model;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by parker on 2015/12/6.
 */
@Deprecated
class _myDateformat extends DateFormat {
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        toAppendTo.append(c.get(Calendar.YEAR)); toAppendTo.append("/");
        toAppendTo.append(c.get(Calendar.MONTH)+1); toAppendTo.append("/");
        toAppendTo.append(c.get(Calendar.DAY_OF_MONTH)); toAppendTo.append(" ");
        toAppendTo.append(c.get(Calendar.HOUR_OF_DAY)); toAppendTo.append(":");
        toAppendTo.append(c.get(Calendar.MINUTE)); toAppendTo.append(":");
        toAppendTo.append(c.get(Calendar.SECOND)); toAppendTo.append("_");
        toAppendTo.append(c.get(Calendar.MILLISECOND));
        return toAppendTo;
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        return null;
    }
}
