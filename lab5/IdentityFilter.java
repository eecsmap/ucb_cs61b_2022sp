/**
 * TableFilter which does not filter out any rows.
 *
 * @author Matthew Owen
 */
public class IdentityFilter extends TableFilter {

    public IdentityFilter(Table input) {
        super(input);
    }

    @Override
    protected boolean keep() {
        return true;
    }
}
