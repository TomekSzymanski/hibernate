package web;

import java.util.List;

/**
 * Created on 2014-12-18.
 */
public class HTMLTable implements Printable {
    private StringBuilder out = new StringBuilder();

    public HTMLTable(String tableCaption, List<String> tableHeaders, List<List<String>> tableCells) {
        out.append("<div>\n");
        out.append("<table>\n");

        out.append("<caption>" + tableCaption + "</caption>\n");

        out.append("<thead>\n");
        out.append("<tr>\n");
        for (String header : tableHeaders) {
            out.append("<th>" + header + "</th>\n");
        }
        out.append("</tr>\n");
        out.append("</thead>\n");

        for (List<String> tableCellRow : tableCells) {
            out.append("<tr>\n");
            for (String tableCell : tableCellRow) {
                out.append("<td>" + tableCell + "</td>\n");
            }
            out.append("</tr>\n");
        }

        out.append("</table>\n");
        out.append("</div>\n");
    }

    @Override
    public String toString() {
        return out.toString();
    }

}
