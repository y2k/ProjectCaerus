package android.os;

import com.android.server.am.ActivityManagerService;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.io.FileDescriptor;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by y2k on 6/2/16.
 */
public class StubServiceManager implements IServiceManager {

    private final Map<String, IBinder> cache = new ConcurrentHashMap<>();

    @Override
    public IBinder getService(String s) throws RemoteException {
        return new IBinder() {
            @Override
            public String getInterfaceDescriptor() throws RemoteException {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean pingBinder() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isBinderAlive() {
                throw new UnsupportedOperationException();
            }

            @Override
            public IInterface queryLocalInterface(String s) {
//                if (cache.containsKey(s)) return cache.get(s);
                if (Objects.equals(s, "android.app.IActivityManager")) {
                    ActivityManagerService.main(0);
                    return ActivityManagerService.self();
                } else if (Objects.equals(s, "android.os.IPowerManager")) {
                    return Mockito.mock(
                        android.os.IPowerManager.class,
                        (Answer) invocation -> {
                            throw new UnsupportedOperationException("method = " + invocation);
                        });
                }
                throw new UnsupportedOperationException("name = " + s);
            }

            @Override
            public void dump(FileDescriptor fileDescriptor, String[] strings) throws RemoteException {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean transact(int i, Parcel parcel, Parcel parcel1, int i1) throws RemoteException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void linkToDeath(DeathRecipient deathRecipient, int i) throws RemoteException {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean unlinkToDeath(DeathRecipient deathRecipient, int i) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public IBinder checkService(String s) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addService(String s, IBinder iBinder) throws RemoteException {
        cache.put(s, iBinder);
    }

    @Override
    public String[] listServices() throws RemoteException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPermissionController(IPermissionController iPermissionController) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    @Override
    public IBinder asBinder() {
        throw new UnsupportedOperationException();
    }
}
