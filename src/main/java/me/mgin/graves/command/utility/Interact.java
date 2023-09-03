package me.mgin.graves.command.utility;

import me.mgin.graves.util.Responder;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class Interact {
    /**
     * Generates a list of pages that the player can interact with. The command should
     * be the one used for switching pages and should always contain a %d flag to work
     * properly. The index of the page will be passed to it via String.format.
     *
     * @param res Responder
     * @param page int
     * @param amountOfPages int
     * @param command String
     * @return Text
     */
    public static Text generatePagination(Responder res, int page, int amountOfPages, String command) {
        Text pagination = new LiteralText("Page: ");

        // Pages will always start at 1
        for (int i = 1; i <= amountOfPages; i++) {
            // Handles the currently displayed page
            if (page == i) {
                pagination = pagination.shallowCopy().append(res.hoverText(
                    res.info(String.format("[%d] ", i)),
                    new TranslatableText("utility.pagination.current-page.tooltip")
                ));
                continue;
            }

            pagination = pagination.shallowCopy().append(generateButton(res,
                res.highlight(String.format("%d ", i)),
                new TranslatableText("utility.pagination.page-id.tooltip", i),
                String.format(command, i)
            ));
        }

        return pagination;
    }

    public static Text generateButton(Responder res, Text message, Text hoverContent, String command) {
        return res.runOnClick(res.hoverText(message, hoverContent), command);
    }
}
