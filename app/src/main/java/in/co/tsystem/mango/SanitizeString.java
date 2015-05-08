package in.co.tsystem.mango;

/**
 * Created by achatterjee on 09-05-2015.
 */
public class SanitizeString {
    public String mstring;

    public SanitizeString(String str) {
        mstring = str;
        removeAllTags();
    }

    public boolean containsTags()
    {
        boolean lt, gt;

        lt = mstring.contains("&lt;");
        gt = mstring.contains("&gt;");

        return (lt || gt);
    }

    public void removeTags()
    {
        int indexof_lt, indexof_gt;

        indexof_lt = mstring.indexOf("&lt;");
        indexof_gt = mstring.indexOf("&gt;");

        String str_first, str_last;

        str_first = mstring.substring(0, indexof_lt);
        str_last = mstring.substring(indexof_gt + 4 , mstring.length());

        mstring = str_first + str_last;
    }

    public void removeAllTags()
    {
        while(containsTags())
        {
            removeTags();
        }
    }
}
