package limited.it.planet.incomingcallrecordapp.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by interspeed.com.bd on 03-Apr-17.
 */
public class FontCustomization {
    public Typeface TexgyreHerosRegular;
    public Typeface TexgyreHerosBold;
    Context context;

    public FontCustomization(Context mContext){
        this.context = mContext;
        this.TexgyreHerosRegular = Typeface.createFromAsset(context.getAssets(),"texgyreheros-regular.otf") ;
        this.TexgyreHerosBold = Typeface.createFromAsset(context.getAssets(),"texgyreheros-bold.otf") ;

    }
    public Typeface getTexgyreHerosRegular(){
        return TexgyreHerosRegular;
    }
    public Typeface getTexgyreHerosBold(){
        return TexgyreHerosBold;
    }


}
