package server.utility;

import common.Request;
import common.Response;
import common.ResponseCode;
import common.User;
import common.data.Flat;

//Класс для обработки запроса
public class RequestManager {
    private CommandManager commandManager;
    private CollectionManager collectionManager;
    public RequestManager(CommandManager commandManager,CollectionManager collectionManager) {
        this.commandManager = commandManager;
        this.collectionManager=collectionManager;
    }

    public RequestManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    public Response manage(Request request){
        User hashUser=new User(request.getUser().getLogin(), request.getUser().getPassword());
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getArgument(), request.getObjectArgument(), hashUser);
        if (request.getCommandName().equals("show")) return new Response(responseCode, ResponseCreator.getAndClear(), collectionManager.getCollection());
        return new Response(responseCode, ResponseCreator.getAndClear());

    }
    private synchronized ResponseCode executeCommand(String command, String argument, Flat flat, User user) {
        switch (command) {
            case "":
                break;
            case "help":
                if (!commandManager.help(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "info":
                if (!commandManager.info(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "show":
                if (!commandManager.show(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "insert":
                if (!commandManager.insert(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.update(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_key":
                if (!commandManager.removeKey(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "execute_script":
                if (!commandManager.executeScript(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(argument, flat, user))
                    return ResponseCode.ERROR;
                break;//return ResponseCode.CLIENT_EXIT;
            case "replace_if_greater":
                if (!commandManager.replaceIfGreater(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "replace_if_lower":
                if (!commandManager.replaceIfLower(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_lower_key":
                if (!commandManager.removeLowerKey(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_all_by_number_of_rooms":
                if (!commandManager.removeAllByNumber(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "count_greater_than_furnish":
                if (!commandManager.countFurnish(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "filter_starts_with_name":
                if (!commandManager.filterName(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "server_exit":
                if (!commandManager.serverExit(argument, flat, user))
                    return ResponseCode.ERROR;
                return ResponseCode.SERVER_EXIT;
            case "login":
                if (!commandManager.login(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            case "register":
                if (!commandManager.register(argument, flat, user))
                    return ResponseCode.ERROR;
                break;
            default:
                ResponseCreator.appendln("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                return ResponseCode.ERROR;
        }
        return ResponseCode.OK;
    }
}
