package protect.card_locker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;
import protect.card_locker.preferences.Settings;

public class GroupCursorAdapter extends BaseCursorAdapter<GroupCursorAdapter.GroupListItemViewHolder> {
    Settings mSettings;
    private final Context mContext;
    private final GroupAdapterListener mListener;
    DBHelper mDb;

    public GroupCursorAdapter(Context inputContext, Cursor inputCursor, GroupAdapterListener inputListener) {
        super(inputCursor, DBHelper.LoyaltyCardDbGroups.ORDER);
        setHasStableIds(true);
        mSettings = new Settings(inputContext);
        mContext = inputContext.getApplicationContext();
        mListener = inputListener;
        mDb = new DBHelper(inputContext);

        swapCursor(inputCursor);
    }

    @NonNull
    @Override
    public GroupCursorAdapter.GroupListItemViewHolder onCreateViewHolder(ViewGroup inputParent, int inputViewType) {
        View itemView = LayoutInflater.from(inputParent.getContext()).inflate(R.layout.group_layout, inputParent, false);
        return new GroupListItemViewHolder(itemView);
    }

    public void onBindViewHolder(GroupListItemViewHolder inputHolder, Cursor inputCursor) {
        Group group = Group.toGroup(inputCursor);

        inputHolder.mName.setText(group._id);

        int groupCardCount = mDb.getGroupCardCount(group._id);
        inputHolder.mCardCount.setText(mContext.getResources().getQuantityString(R.plurals.groupCardCount, groupCardCount, groupCardCount));

        inputHolder.mName.setTextSize(mSettings.getFontSizeMax(mSettings.getMediumFont()));
        inputHolder.mCardCount.setTextSize(mSettings.getFontSizeMax(mSettings.getSmallFont()));

        applyClickEvents(inputHolder);
    }

    private void applyClickEvents(GroupListItemViewHolder inputHolder) {
        inputHolder.mMoveDown.setOnClickListener(view -> mListener.onMoveDownButtonClicked(inputHolder.itemView));
        inputHolder.mMoveUp.setOnClickListener(view -> mListener.onMoveUpButtonClicked(inputHolder.itemView));
        inputHolder.mEdit.setOnClickListener(view -> mListener.onEditButtonClicked(inputHolder.itemView));
        inputHolder.mDelete.setOnClickListener(view -> mListener.onDeleteButtonClicked(inputHolder.itemView));
    }

    public interface GroupAdapterListener {
        void onMoveDownButtonClicked(View view);

        void onMoveUpButtonClicked(View view);

        void onEditButtonClicked(View view);

        void onDeleteButtonClicked(View view);
    }

    public static class GroupListItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mCardCount;
        public AppCompatImageButton mMoveUp, mMoveDown, mEdit, mDelete;

        public GroupListItemViewHolder(View inputView) {
            super(inputView);
            mName = inputView.findViewById(R.id.name);
            mCardCount = inputView.findViewById(R.id.cardCount);
            mMoveUp = inputView.findViewById(R.id.moveUp);
            mMoveDown = inputView.findViewById(R.id.moveDown);
            mEdit = inputView.findViewById(R.id.edit);
            mDelete = inputView.findViewById(R.id.delete);
        }
    }
}
