package HTTP;

import manager.ManagerSaveException;

public class KvTaskClientException extends ManagerSaveException {
    public KvTaskClientException(final String message) {
        super(message);
    }
}

