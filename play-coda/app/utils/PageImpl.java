package utils;

import java.util.List;

import com.avaje.ebean.Page;

/**
 * Page that provides transformed items, but delegates for aggregation data to an underlying page.
 */
public class PageImpl<T> implements Page<T> {

    private final List<T> list;
    private final Page<?> delegate;

    public PageImpl(List<T> list, Page<?> delegate) {
        this.list = list;
        this.delegate = delegate;
    }

    @SuppressWarnings("unchecked")
    public static <A, B extends A> Page<A> of(List<B> list, Page<?> delegate) {
        return (Page<A>) new PageImpl<B>(list, delegate);
    }

    @Override
    public List<T> getList() {
        return list;
    }

    @Override
    public int getTotalRowCount() {
        return delegate.getTotalRowCount();
    }

    @Override
    public int getTotalPageCount() {
        return delegate.getTotalPageCount();
    }

    @Override
    public int getPageIndex() {
        return delegate.getPageIndex();
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public boolean hasPrev() {
        return delegate.hasPrev();
    }

    @Override
    public Page<T> next() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<T> prev() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDisplayXtoYofZ(String to, String of) {
        return delegate.getDisplayXtoYofZ(to, of);
    }

}
