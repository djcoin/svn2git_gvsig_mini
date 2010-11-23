
package es.prodevelop.gvsig.mini.search.view;

/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2010 Prodevelop.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *   Prodevelop, S.L.
 *   Pza. Don Juan de Villarrasa, 14 - 5
 *   46001 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   prode@prodevelop.es
 *   http://www.prodevelop.es
 *
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es
 */


import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.search.MapPreview;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;

/**
 * A custom view for an item in the contact list.
 */
public class POIItemView extends ViewGroup {

	private final SearchActivity mContext;

	// private final int mPreferredHeight;
	// private final int mVerticalDividerMargin;
	// private final int mPaddingTop;
	// private final int mPaddingRight;
	// private final int mPaddingBottom;
	// private final int mPaddingLeft;
	// private final int mGapBetweenImageAndText;
	// private final int mGapBetweenLabelAndData;
	// private final int mCallButtonPadding;
	// private final int mPresenceIconMargin;
	// private final int mHeaderTextWidth;
	//
	// private boolean mHorizontalDividerVisible;
	// private Drawable mHorizontalDividerDrawable;
	// private int mHorizontalDividerHeight;
	//
	// private boolean mVerticalDividerVisible;
	// private Drawable mVerticalDividerDrawable;
	// private int mVerticalDividerWidth;
	//
	private boolean mHeaderVisible;
	private Drawable mHeaderBackgroundDrawable;
	private int mHeaderBackgroundHeight;
	TextView mHeaderTextView;
	//
	// private QuickContactBadge mQuickContact;
	// private ImageView mPhotoView;
	// private TextView mNameTextView;
	// private DontPressWithParentImageView mCallButton;
	// private TextView mLabelView;
	// private TextView mDataView;
	// private TextView mSnippetView;
	// private ImageView mPresenceIcon;
	//
	// private int mPhotoViewWidth;
	// private int mPhotoViewHeight;
	// private int mLine1Height;
	// private int mLine2Height;
	// private int mLine3Height;
	//
	// private OnClickListener mCallButtonClickListener;

	TextView mDesc;
	TextView mDist;
	ImageView mImg;
	MapPreview preview;
	Button mOptionsButton;
	Button mDetailsButton;
	LinearLayout convertView;
	LinearLayout l;

	public POIItemView(final SearchActivity context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		convertView = (LinearLayout) mContext.getLayoutInflater().inflate(
				R.layout.street_row, null);

		mDesc = (TextView) convertView.findViewById(R.id.desc);
		mDist = (TextView) convertView.findViewById(R.id.dist);
		mImg = (ImageView) convertView.findViewById(R.id.img);
		mHeaderTextView = (TextView) convertView
				.findViewById(R.id.section_text);

		try {
			l = new LinearLayout(context);
			final RelativeLayout.LayoutParams zzParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			zzParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			zzParams.addRule(RelativeLayout.CENTER_VERTICAL);
			// zzParams.setMargins(20, 20, 20, 20);
			preview = new MapPreview(context, CompatManager.getInstance()
					.getRegisteredContext(), mContext.metrics.widthPixels,
					mContext.metrics.heightPixels / 2);
			l.addView(preview);
			((LinearLayout) ((LinearLayout) convertView)
					.findViewById(R.id.map_preview)).addView(l, zzParams);

			mOptionsButton = (Button) convertView
					.findViewById(R.id.show_options);
			mDetailsButton = (Button) convertView
					.findViewById(R.id.show_details);
			mOptionsButton.setFocusable(false);
			mDetailsButton.setFocusable(false);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TypedArray a = context.obtainStyledAttributes(null,
		// com.android.internal.R.styleable.Theme);
		// mPreferredHeight = a.getDimensionPixelSize(
		// android.R.styleable.Theme_listPreferredItemHeight, 0);
		// a.recycle();
		//
		// Resources resources = context.getResources();
		// mVerticalDividerMargin = resources
		// .getDimensionPixelOffset(R.dimen.list_item_vertical_divider_margin);
		// mPaddingTop = resources
		// .getDimensionPixelOffset(R.dimen.list_item_padding_top);
		// mPaddingBottom = resources
		// .getDimensionPixelOffset(R.dimen.list_item_padding_bottom);
		// mPaddingLeft = resources
		// .getDimensionPixelOffset(R.dimen.list_item_padding_left);
		// mPaddingRight = resources
		// .getDimensionPixelOffset(R.dimen.list_item_padding_right);
		// mGapBetweenImageAndText = resources
		// .getDimensionPixelOffset(R.dimen.list_item_gap_between_image_and_text);
		// mGapBetweenLabelAndData = resources
		// .getDimensionPixelOffset(R.dimen.list_item_gap_between_label_and_data);
		// mCallButtonPadding = resources
		// .getDimensionPixelOffset(R.dimen.list_item_call_button_padding);
		// mPresenceIconMargin = resources
		// .getDimensionPixelOffset(R.dimen.list_item_presence_icon_margin);
		// mHeaderTextWidth = resources
		// .getDimensionPixelOffset(R.dimen.list_item_header_text_width);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// We will match parent's width and wrap content vertically, but make
		// sure
		// height is no less than listPreferredItemHeight.
		// int width = resolveSize(0, widthMeasureSpec);
		// int height = 0;
		//
		// mLine1Height = 0;
		// mLine2Height = 0;
		// mLine3Height = 0;
		//
		// // Obtain the natural dimensions of the name text (we only care about
		// // height)
		// mNameTextView.measure(0, 0);
		//
		// mLine1Height = mNameTextView.getMeasuredHeight();
		//
		// if (isVisible(mLabelView)) {
		// mLabelView.measure(0, 0);
		// mLine2Height = mLabelView.getMeasuredHeight();
		// }
		//
		// if (isVisible(mDataView)) {
		// mDataView.measure(0, 0);
		// mLine2Height = Math
		// .max(mLine2Height, mDataView.getMeasuredHeight());
		// }
		//
		// if (isVisible(mSnippetView)) {
		// mSnippetView.measure(0, 0);
		// mLine3Height = mSnippetView.getMeasuredHeight();
		// }
		//
		// height += mLine1Height + mLine2Height + mLine3Height;
		//
		// if (isVisible(mCallButton)) {
		// mCallButton.measure(0, 0);
		// }
		// if (isVisible(mPresenceIcon)) {
		// mPresenceIcon.measure(0, 0);
		// }
		//
		// ensurePhotoViewSize();
		//
		// height = Math.max(height, mPhotoViewHeight);
		// height = Math.max(height, mPreferredHeight);
		//
		// if (mHeaderVisible) {
		// ensureHeaderBackground();
		// mHeaderTextView.measure(MeasureSpec.makeMeasureSpec(
		// mHeaderTextWidth, MeasureSpec.EXACTLY), MeasureSpec
		// .makeMeasureSpec(mHeaderBackgroundHeight,
		// MeasureSpec.EXACTLY));
		// height += mHeaderBackgroundDrawable.getIntrinsicHeight();
		// }

		ensureHeaderBackground();
		setMeasuredDimension(convertView.getWidth(), convertView.getHeight());
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		int height = bottom - top;
		int width = right - left;

		convertView.layout(left, top, right, bottom);
		// Determine the vertical bounds by laying out the header first.
		int topBound = 0;

		if (mHeaderVisible) {
			mHeaderBackgroundDrawable.setBounds(0, 0, width,
					mHeaderBackgroundHeight);
			mHeaderTextView.layout(0, 0, width, mHeaderBackgroundHeight);
			topBound += mHeaderBackgroundHeight;
		}

		// // Positions of views on the left are fixed and so are those on the
		// right side.
		// // The stretchable part of the layout is in the middle. So, we will
		// start off
		// // by laying out the left and right sides. Then we will allocate the
		// remainder
		// // to the text fields in the middle.
		//
		// // Left side
		// int leftBound = mPaddingLeft;
		// View photoView = mQuickContact != null ? mQuickContact : mPhotoView;
		// if (photoView != null) {
		// // Center the photo vertically
		// int photoTop = topBound + (height - topBound - mPhotoViewHeight) / 2;
		// photoView.layout(
		// leftBound,
		// photoTop,
		// leftBound + mPhotoViewWidth,
		// photoTop + mPhotoViewHeight);
		// leftBound += mPhotoViewWidth + mGapBetweenImageAndText;
		// }
		//
		// // Right side
		// int rightBound = right;
		// if (isVisible(mCallButton)) {
		// int buttonWidth = mCallButton.getMeasuredWidth();
		// rightBound -= buttonWidth;
		// mCallButton.layout(
		// rightBound,
		// topBound,
		// rightBound + buttonWidth,
		// height);
		// mVerticalDividerVisible = true;
		// ensureVerticalDivider();
		// rightBound -= mVerticalDividerWidth;
		// mVerticalDividerDrawable.setBounds(
		// rightBound,
		// topBound + mVerticalDividerMargin,
		// rightBound + mVerticalDividerWidth,
		// height - mVerticalDividerMargin);
		// } else {
		// mVerticalDividerVisible = false;
		// }
		//
		// if (isVisible(mPresenceIcon)) {
		// int iconWidth = mPresenceIcon.getMeasuredWidth();
		// rightBound -= mPresenceIconMargin + iconWidth;
		// mPresenceIcon.layout(
		// rightBound,
		// topBound,
		// rightBound + iconWidth,
		// height);
		// }
		//
		// if (mHorizontalDividerVisible) {
		// ensureHorizontalDivider();
		// mHorizontalDividerDrawable.setBounds(
		// 0,
		// height - mHorizontalDividerHeight,
		// width,
		// height);
		// }
		//
		// topBound += mPaddingTop;
		// int bottomBound = height - mPaddingBottom;
		//
		// // Text lines, centered vertically
		// rightBound -= mPaddingRight;
		//
		// // Center text vertically
		// int totalTextHeight = mLine1Height + mLine2Height + mLine3Height;
		// int textTopBound = (bottomBound + topBound - totalTextHeight) / 2;
		//
		// mNameTextView.layout(leftBound,
		// textTopBound,
		// rightBound,
		// textTopBound + mLine1Height);
		//
		// int dataLeftBound = leftBound;
		// if (isVisible(mLabelView)) {
		// dataLeftBound = leftBound + mLabelView.getMeasuredWidth();
		// mLabelView.layout(leftBound,
		// textTopBound + mLine1Height,
		// dataLeftBound,
		// textTopBound + mLine1Height + mLine2Height);
		// dataLeftBound += mGapBetweenLabelAndData;
		// }
		//
		// if (isVisible(mDataView)) {
		// mDataView.layout(dataLeftBound,
		// textTopBound + mLine1Height,
		// rightBound,
		// textTopBound + mLine1Height + mLine2Height);
		// }
		//
		// if (isVisible(mSnippetView)) {
		// mSnippetView.layout(leftBound,
		// textTopBound + mLine1Height + mLine2Height,
		// rightBound,
		// textTopBound + mLine1Height + mLine2Height + mLine3Height);
		// }
	}

	private boolean isVisible(View view) {
		return view != null && view.getVisibility() == View.VISIBLE;
	}

	/**
	 * Loads the drawable for the header background if it has not yet been
	 * loaded.
	 */
	private void ensureHeaderBackground() {
		if (mHeaderBackgroundDrawable == null) {
			mHeaderBackgroundDrawable = mContext.getResources().getDrawable(
					android.R.drawable.dark_header);
			mHeaderBackgroundHeight = mHeaderBackgroundDrawable
					.getIntrinsicHeight();
		}
	}

	@Override
	public void dispatchDraw(Canvas canvas) {
		if (mHeaderVisible) {
			mHeaderBackgroundDrawable.draw(canvas);
		}

		super.dispatchDraw(canvas);
	}

	/**
	 * Sets section header or makes it invisible if the title is null.
	 */
	public void setSectionHeader(String title) {
		if (!TextUtils.isEmpty(title)) {
			mHeaderTextView.setText(title);
			mHeaderTextView.setVisibility(View.VISIBLE);
			mHeaderVisible = true;
		} else {
			if (mHeaderTextView != null) {
				mHeaderTextView.setVisibility(View.GONE);
			}
			mHeaderVisible = false;
		}
	}
}
