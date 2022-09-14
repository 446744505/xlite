package xlite.type;

public abstract class TypeBase implements XType {
    TypeBase() {}

    @Override
    public boolean isBase() {
        return true;
    }
}
