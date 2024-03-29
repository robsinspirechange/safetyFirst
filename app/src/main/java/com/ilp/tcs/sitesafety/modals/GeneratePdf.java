package com.ilp.tcs.sitesafety.modals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ilp.tcs.sitesafety.R;
import com.ilp.tcs.sitesafety.activity.SiteSafetyApplication;
import com.ilp.tcs.sitesafety.preference.SitePreference;
import com.ilp.tcs.sitesafety.utils.Util;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by 1094048 17-8-2016
 * GeneratePdf
 * This class is generating PDF file which can be used to display user input data into pdf form.
 */

public class GeneratePdf extends AsyncTask<Void, Void, Void> {
    /**
     * It is a Class which is used to generate pdf file, it writes the input data into the document.
     */
    private PdfWriter writer;

    /**
     * It is a string type variable that holds the date value so that system date can be used in header and footer.
     */
    private String date;

    /**
     * it an ArrayList of GroupItem that consits of id,title,image etc.
     */
    private ArrayList<GroupItem> groupItemsArrayList = new ArrayList<>();

    /**
     * This will tell from which class it belongs.
     */
    private Context mContext;

    private Survey mSurvey;

    /**
     * An "abstract" representation of a file system entity identified by a
     * pathname.
     */
    private File mFile;

    /**
     * Dialog to show while generating PDF document
     */
    private ProgressDialog mDialog;

    /**
     * @param groupItemsArrayList this holds the values inside an ArrayList so that can be used in file generation.
     * @param context             Context of the component
     * @param file                An "abstract" representation of a file system entity identified by a
     *                            pathname.
     */
    public GeneratePdf(Survey survey, ArrayList<GroupItem> groupItemsArrayList, Context context, File file) {
        this.groupItemsArrayList = groupItemsArrayList;
        this.mContext = context;
        this.mFile = file;
        this.mSurvey = survey;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Generating Report");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mDialog.dismiss();
        Toast.makeText(mContext, "Report generated", Toast.LENGTH_SHORT).show();
//        DbHelper.logPrintTable(TblSurvey.TABLE_NAME);
    }

    /**
     * @param document A type of class that consists of all the methods involved in pdf file generation.
     * @throws DocumentException It ia type of Exception that throws a document exception.
     */
    public void addTitlePage(Document document) throws DocumentException {

        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Font medium = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

        try {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            date = sdf.format(calendar.getTime());

            document.add(new Paragraph("                                Site Safety Management Audit and Review - Report", smallBold));
            document.add(new Paragraph("           Done By: " + mSurvey.getUserName() + "                                                                  Date: " + sdf.format(mSurvey.getCreatedDateTime()), normal));
            document.add(new Paragraph("           Branch: " + SitePreference.getInstance().getBranchName() + "                                                                 Updated on: " + sdf.format(mSurvey.getUpdatedDateTime()), normal));
            document.add(new Paragraph("           Location: " + SitePreference.getInstance().getLocationName(), normal));
            document.add(new Paragraph(".", medium));

            float[] coloumnWidths = {3, 1, 1, 2, 1};
            PdfPTable table = new PdfPTable(coloumnWidths);
            PdfPCell pp = new PdfPCell(new Phrase("Audit"));
            pp.setBackgroundColor(new BaseColor(184, 187, 191));
            PdfPCell pp1 = new PdfPCell(new Phrase("Element"));
            pp1.setBackgroundColor(new BaseColor(184, 187, 191));
            PdfPCell pp2 = new PdfPCell(new Phrase("Complaint"));
            pp2.setBackgroundColor(new BaseColor(184, 187, 191));
            PdfPCell pp3 = new PdfPCell(new Phrase("Remarks"));
            pp3.setBackgroundColor(new BaseColor(184, 187, 191));
            PdfPCell pp4 = new PdfPCell(new Phrase("Appendix"));
            pp4.setBackgroundColor(new BaseColor(184, 187, 191));
            table.addCell(pp);
            table.addCell(pp1);
            table.addCell(pp2);
            table.addCell(pp3);
            table.addCell(pp4);
            table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
/*Author : Abhishek Gupta
* TODO*/
            PdfPCell a0 = new PdfPCell(new Phrase(new Phrase("I.       Building - External", smallBold)));
            a0.setBorder(Rectangle.BOTTOM);
            a0.setBackgroundColor(BaseColor.YELLOW);
            PdfPCell b0 = new PdfPCell(new Phrase(""));
            b0.setBackgroundColor(BaseColor.YELLOW);
            b0.setBorder(Rectangle.BOTTOM);
            PdfPCell c0 = new PdfPCell(new Phrase(""));
            c0.setBackgroundColor(BaseColor.YELLOW);
            c0.setBorder(Rectangle.BOTTOM);
            PdfPCell d0 = new PdfPCell(new Phrase(""));
            d0.setBackgroundColor(BaseColor.YELLOW);
            d0.setBorder(Rectangle.BOTTOM);
            table.addCell(a0);
            table.addCell(b0);
            table.addCell(c0);
            table.addCell(d0);
            table.addCell(d0);

            String check;

            for (int i = 0; i < groupItemsArrayList.size(); i++) {
                GroupItem groupItem = groupItemsArrayList.get(i);
                int flag = 1;
                ArrayList<ChildItem> childItems = groupItem.getChildItems();

                check = groupItem.getTitle().substring(0,2);
                if (check.equals("10")){
                    PdfPCell a1 = new PdfPCell(new Phrase(new Phrase("II.      Building - Internal", smallBold)));
                    a1.setBorder(Rectangle.BOTTOM);
                    a1.setBackgroundColor(BaseColor.YELLOW);
                    PdfPCell b1 = new PdfPCell(new Phrase(""));
                    b1.setBackgroundColor(BaseColor.YELLOW);
                    b1.setBorder(Rectangle.BOTTOM);
                    PdfPCell c1 = new PdfPCell(new Phrase(""));
                    c1.setBackgroundColor(BaseColor.YELLOW);
                    c1.setBorder(Rectangle.BOTTOM);
                    PdfPCell d1 = new PdfPCell(new Phrase(""));
                    d1.setBackgroundColor(BaseColor.YELLOW);
                    d1.setBorder(Rectangle.BOTTOM);
                    table.addCell(a1);
                    table.addCell(b1);
                    table.addCell(c1);
                    table.addCell(d1);
                    table.addCell(d1);
                }
                else if (check.equals("21")){
                    PdfPCell a2 = new PdfPCell(new Phrase(new Phrase("II.      Processes & Practices", smallBold)));
                    a2.setBorder(Rectangle.BOTTOM);
                    a2.setBackgroundColor(BaseColor.YELLOW);
                    PdfPCell b2 = new PdfPCell(new Phrase(""));
                    b2.setBackgroundColor(BaseColor.YELLOW);
                    b2.setBorder(Rectangle.BOTTOM);
                    PdfPCell c2 = new PdfPCell(new Phrase(""));
                    c2.setBackgroundColor(BaseColor.YELLOW);
                    c2.setBorder(Rectangle.BOTTOM);
                    PdfPCell d2 = new PdfPCell(new Phrase(""));
                    d2.setBackgroundColor(BaseColor.YELLOW);
                    d2.setBorder(Rectangle.BOTTOM);
                    table.addCell(a2);
                    table.addCell(b2);
                    table.addCell(c2);
                    table.addCell(d2);
                    table.addCell(d2);

                }

                PdfPCell a = new PdfPCell(new Phrase(new Phrase(groupItem.getTitle(), smallBold)));
                a.setBorder(Rectangle.NO_BORDER);
                a.setBackgroundColor(BaseColor.GRAY);
                PdfPCell b = new PdfPCell(new Phrase(""));
                b.setBackgroundColor(BaseColor.GRAY);
                b.setBorder(Rectangle.NO_BORDER);
                PdfPCell c = new PdfPCell(new Phrase(""));
                c.setBackgroundColor(BaseColor.GRAY);
                c.setBorder(Rectangle.NO_BORDER);
                PdfPCell d = new PdfPCell(new Phrase(""));
                d.setBackgroundColor(BaseColor.GRAY);
                d.setBorder(Rectangle.NO_BORDER);
                table.addCell(a);
                table.addCell(b);
                table.addCell(c);
                table.addCell(d);
                table.addCell(d);


                int count = 0;
                for (ChildItem childItem : childItems) {
                    PdfPCell p = new PdfPCell();
                    p.setPadding(10);
                    if (childItem.getResponse() == 1) {
                        p.setBackgroundColor(new BaseColor(119, 242, 137));
                        p.addElement(new Phrase("YES"));
                    } else if (childItem.getResponse() == -1) {
                        p.setBackgroundColor(new BaseColor(242, 10, 10));
                        p.addElement(new Phrase("NO"));
                        count = 1;
                    } else {
                        p.setBackgroundColor(new BaseColor(38, 107, 235));
                        p.addElement(new Phrase("NA"));
                    }
                    table.addCell(new Phrase(childItem.getTitle(), normal));
                    table.addCell(new Phrase(childItem.getElement(), normal));
                    table.addCell(p);
                    table.addCell(childItem.getDescription());
                    if(count != 0) {
                        Log.i("Details:", "Titles: " + childItem.getTitle() + "\nElements: " + childItem.getElement() + "\nDescription: " + childItem.getDescription());
                        count=0;
                    }

                    table.addCell(new Phrase("Appendix" + (i + 1) + "." + flag, medium));
                    table.setWidthPercentage(100);
                    flag++;
                }

            }
            document.add(table);
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            PdfPTable calculations = writeCalculations();
            document.add(calculations);

            int apFlag=1;
            int apFlag2=1;
            for (int i = 0; i < groupItemsArrayList.size(); i++) {
                GroupItem groupItem = groupItemsArrayList.get(i);
                ArrayList<ChildItem> childItems = groupItem.getChildItems();
               apFlag=1;
                for (ChildItem childItem : childItems) {
                    apFlag2=1;
                    ArrayList<com.ilp.tcs.sitesafety.modals.Image> ar = childItem.getImages();
                    for (com.ilp.tcs.sitesafety.modals.Image image : ar) {
                     //  BitmapFactory.Optio=ns bmOptions = new BitmapFactory.Options();
                       // Bitmap resizedBitmap = BitmapFactory.decodeFile(image.getImagePath(),bmOptions);
                        Bitmap resizedBitmap = Util.getScaledBitmap(Util.removeFileFromPath(image.getImagePath()), 300, 150);
                       // resizedBitmap = Util.scaleCenterCrop(resizedBitmap,300,100);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();

                        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        Image image1 = Image.getInstance(stream.toByteArray());

                        PdfContentByte pdfContentByte = writer.getDirectContentUnder();

                        Image img = getWatermarkedImage(pdfContentByte, image1, "Appendix " + (i + 1) + "." + apFlag + "." + apFlag2, image.getCaption());
                        document.add(img);
//                        if (!Util.isEmpty(image.getCaption()))
//                            document.add(new Paragraph(image.getCaption()));
                        apFlag2++;
                    }
                    apFlag++;
          //          Log.v("aaaaa","*********************************");
          //          Log.v("aaaaapFlag", String.valueOf(apFlag));
          //          Log.v("aaaaAPFlag2aaaa", String.valueOf(apFlag2));
          //          Log.v("aaaaa","*********************************");
                }
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param cb        Content byte class for PDF
     * @param img       Abstract class for image
     * @param watermark String to set as watermark
     * @return Instance of the watermarked image
     * @throws DocumentException It ia type of Exception that throws a document exception.
     */
    public Image getWatermarkedImage(PdfContentByte cb, Image img, String watermark, String caption) throws DocumentException {
        Font wm = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL, GrayColor.GRAYWHITE);
        float width = img.getWidth();
        float height = img.getHeight();
        PdfTemplate template = cb.createTemplate(width, height);
        template.addImage(img, width, 0, 0, height, 0, 0);
    //    Log.v("aaaaawatermark",watermark);
        Chunk textAsChunk1 = new Chunk(watermark, wm);
        textAsChunk1.setBackground(new BaseColor(0, 0, 0));
        ColumnText.showTextAligned(template, Element.ALIGN_BOTTOM,
                new Phrase(textAsChunk1), width / 50, height- (height-10), 0);

        if (!Util.isEmpty(caption)) {

            Chunk textAsChunk = new Chunk(caption, wm);
            textAsChunk.setBackground(new BaseColor(0, 0, 0));

            ColumnText.showTextAligned(template, Element.ALIGN_TOP,
                    new Phrase(textAsChunk), width / 50, height - 20, 0);
        }
        return Image.getInstance(template);
    }

    @Override
    protected Void doInBackground(Void... params) {

        Document document = new Document(PageSize.A4, 15, 15, 15, 15);

        try

        {
            writer = PdfWriter.getInstance(document, new FileOutputStream(mFile));
/////////////////////////////////////////////////////HEADER AND FOOTER/////////////////
            MyFooter event = new MyFooter();
            writer.setPageEvent(event);
////////////////////////////////////////////////////////////////////////////////////
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();

        try

        {
            addTitlePage(document);
        } catch (
                DocumentException e
                )

        {
            e.printStackTrace();
        }

        document.close();
        return null;
    }


    /**
     * Class used for generating custom header and footer.
     */
    class MyFooter extends PdfPageEventHelper {
        /**
         * Fonts to use for header and footer
         */
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 10, Font.ITALIC);

        /**
         * @param writer   Pdf writer instance
         * @param document Document to write in
         */
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Phrase footer = new Phrase(date, ffont);
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    footer,
                    (document.right() - document.left()) + 0, 5, 0);
        }
    }


    private PdfPTable writeCalculations(){

        float[] coloumnWidths = {5, 1, 1, 1, 1, 1};
        PdfPTable table = new PdfPTable(coloumnWidths);

        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);

        {
            PdfPCell a1 = new PdfPCell(new Phrase(new Phrase("Calculations", smallBold)));
            a1.setBorder(Rectangle.BOTTOM);
            a1.setBackgroundColor(BaseColor.YELLOW);


            PdfPCell b1 = new PdfPCell(new Phrase(""));
            b1.setBackgroundColor(BaseColor.YELLOW);
            b1.setBorder(Rectangle.BOTTOM);
            PdfPCell c1 = new PdfPCell(new Phrase(""));
            c1.setBackgroundColor(BaseColor.YELLOW);
            c1.setBorder(Rectangle.BOTTOM);
            PdfPCell d1 = new PdfPCell(new Phrase(""));
            d1.setBackgroundColor(BaseColor.YELLOW);
            d1.setBorder(Rectangle.BOTTOM);
            table.addCell(a1);
            table.addCell(b1);
            table.addCell(c1);
            table.addCell(d1);
            table.addCell(d1);
            table.addCell(d1);
        }

        {
            PdfPCell a2 = new PdfPCell(new Phrase(new Phrase("Safety score with weighted categories", smallBold)));
            a2.setBorder(Rectangle.BOTTOM);
            a2.setBackgroundColor(BaseColor.GREEN);


            PdfPCell b1 = new PdfPCell(new Phrase(""));
            b1.setBackgroundColor(BaseColor.GREEN);
            b1.setBorder(Rectangle.BOTTOM);
            PdfPCell c1 = new PdfPCell(new Phrase(""));
            c1.setBackgroundColor(BaseColor.GREEN);
            c1.setBorder(Rectangle.BOTTOM);
            PdfPCell d1 = new PdfPCell(new Phrase(""));
            d1.setBackgroundColor(BaseColor.GREEN);
            d1.setBorder(Rectangle.BOTTOM);
            table.addCell(a2);
            table.addCell(b1);
            table.addCell(c1);
            table.addCell(d1);
            table.addCell(d1);
            table.addCell(d1);


        }
        PdfPCell p = new PdfPCell(new Phrase(""));
        p.setBackgroundColor(new BaseColor(184, 187, 191));
        PdfPCell pp = new PdfPCell(new Phrase("NA"));
        pp.setBackgroundColor(new BaseColor(184, 187, 191));
        PdfPCell pp1 = new PdfPCell(new Phrase("Yes"));
        pp1.setBackgroundColor(new BaseColor(184, 187, 191));
        PdfPCell pp2 = new PdfPCell(new Phrase("No"));
        pp2.setBackgroundColor(new BaseColor(184, 187, 191));
        PdfPCell pp3 = new PdfPCell(new Phrase("Tp"));
        pp3.setBackgroundColor(new BaseColor(184, 187, 191));
        PdfPCell pp4 = new PdfPCell(new Phrase("Score"));
        pp4.setBackgroundColor(new BaseColor(184, 187, 191));

        table.addCell(p);
        table.addCell(pp);
        table.addCell(pp1);
        table.addCell(pp2);
        table.addCell(pp3);
        table.addCell(pp4);
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        Resources resource = SiteSafetyApplication.getContext().getResources();
        ArrayList<String> elements = new ArrayList<>();
        elements.add(resource.getString(R.string.general_safety));
        elements.add(resource.getString(R.string.fire_safety));
        elements.add(resource.getString(R.string.electrical_safety));
        elements.add(resource.getString(R.string.emergency_prep));
        elements.add(resource.getString(R.string.workplace_safety));
        elements.add(resource.getString(R.string.safe_work));
        elements.add(resource.getString(R.string.vehicular_safety));
        elements.add(resource.getString(R.string.contractor_safety));
        elements.add(resource.getString(R.string.health_hygiene));
        elements.add(resource.getString(R.string.Safety_Signage));

        int totalYes = 0, totalNo = 0, totalNa = 0, totalPoints = 0,totalScore=0;
        int totalYes_w = 0, totalNo_w = 0, totalNa_w = 0, totalPoints_w = 0;

        int i;
        for (i = 0; i < elements.size(); i++) {

            int no_w = 0, nan_w = 0, yes_w = 0, tp_w = 0;
            int no=0, nan=0 ,yes=0 , tp=0 ,score;
            for (GroupItem groupItem : groupItemsArrayList) {
                ArrayList<ChildItem> childItems = groupItem.getChildItems();
                for (ChildItem childItem : childItems) {
                    if (childItem.getElement().equals(elements.get(i))) {
                        switch (childItem.getResponse()) {
                            case -1:
                                no_w += childItem.getWeight();
                                no+=1;
                                tp+=1;
                                break;
                            case 0:
                                nan_w += childItem.getWeight();
                                nan+=1;
                                break;
                            case 1:
                                yes_w += childItem.getWeight();
                                yes+=1;
                                tp+=1;
                                break;
                        }
                    }
                }

            }

            totalYes +=yes;
            totalNo+= no;
            totalNa+=nan;
            totalPoints +=tp;
            score = (int) (((float) yes / (yes + no)) * 100);


            PdfPCell c1 = new PdfPCell(new Phrase(new Phrase(elements.get(i))));
            c1.setBorder(Rectangle.NO_BORDER);
            PdfPCell c2 = new PdfPCell(new Phrase(""+nan));
            c2.setBorder(Rectangle.NO_BORDER);
            PdfPCell c3 = new PdfPCell(new Phrase(""+yes));
            c3.setBorder(Rectangle.NO_BORDER);
            PdfPCell c4 = new PdfPCell(new Phrase(""+no));
            c4.setBorder(Rectangle.NO_BORDER);
            PdfPCell c5 = new PdfPCell(new Phrase(""+tp));
            c5.setBorder(Rectangle.NO_BORDER);
            PdfPCell c6 = new PdfPCell(new Phrase(""+score+"%"));
            c6.setBorder(Rectangle.NO_BORDER);

         //   Log.i("Scoer","nan="+nan+" yes="+yes+" no="+no+" tp="+tp+" score="+score);
           // Log.i("Scoer","totalNa="+totalNa+" totalYes="+totalYes+" totalNo="+totalNo+" totalPoints="+totalPoints+" totalScore="+totalScore);

            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
            table.addCell(c4);
            table.addCell(c5);
            table.addCell(c6);

        }


        PdfPCell c1 = new PdfPCell(new Phrase(new Phrase("")));
        c1.setBorder(Rectangle.TOP);
        PdfPCell c2 = new PdfPCell(new Phrase(new Phrase(""+totalNa)));
        c2.setBorder(Rectangle.TOP);
        PdfPCell c3 = new PdfPCell(new Phrase(new Phrase(""+totalYes)));
        c3.setBorder(Rectangle.TOP);
        PdfPCell c4 = new PdfPCell(new Phrase(new Phrase(""+totalNo)));
        c4.setBorder(Rectangle.TOP);
        PdfPCell c5 = new PdfPCell(new Phrase(new Phrase(""+totalPoints)));
        c5.setBorder(Rectangle.TOP);
        PdfPCell c6 = new PdfPCell(new Phrase(new Phrase("")));
        c6.setBorder(Rectangle.TOP);

        totalScore = (int) ( ((float) totalYes / (totalYes + totalNo)) * 100 );

        table.addCell(c1);
        table.addCell(c2);
        table.addCell(c3);
        table.addCell(c4);
        table.addCell(c5);
        table.addCell(c6);


        PdfPCell o1 = new PdfPCell(new Phrase(new Phrase("Overall Score")));
        o1.setBorder(Rectangle.NO_BORDER);
        o1.setBackgroundColor(BaseColor.RED);
        PdfPCell o2 = new PdfPCell(new Phrase(new Phrase(""+totalScore)));
        o2.setBorder(Rectangle.NO_BORDER);
        o2.setBackgroundColor(new BaseColor(51, 204, 204));
        PdfPCell o3 = new PdfPCell(new Phrase(new Phrase("")));
        o3.setBorder(Rectangle.NO_BORDER);

        table.addCell(o1);
        table.addCell(o2);
        table.addCell(o3);
        table.addCell(o3);
        table.addCell(o3);
        table.addCell(o3);

        return table;






       /* WritableSheet sheet = workbook.createSheet("Calculations", 2);

        WritableFont font_gen = new WritableFont(WritableFont.TIMES);

        WritableFont font_scr_top = new WritableFont(WritableFont.TIMES,11,WritableFont.BOLD);
        font_scr_top.setColour(Colour.BLACK);

        WritableCellFormat cellFormat_gen = new WritableCellFormat(font_gen);
        cellFormat_gen.setBorder(Border.ALL, BorderLineStyle.THIN,Colour.BLACK);


        WritableCellFormat cellFormat_scr_top = new WritableCellFormat(font_scr_top);
        cellFormat_scr_top.setBackground(Colour.RED);
        cellFormat_scr_top.setAlignment(Alignment.LEFT);

        sheet.setColumnView(1,36);

        sheet.addCell(new Label(1, 0, "Safety score with weighted categories",cellFormat_scr_top));

        sheet.addCell(new Label(2, 1, "NA",cellFormat_gen));
        sheet.addCell(new Label(3, 1, "Yes",cellFormat_gen));
        sheet.addCell(new Label(4, 1, "No",cellFormat_gen));
        sheet.addCell(new Label(5, 1, "TP",cellFormat_gen));
        sheet.addCell(new Label(6, 1, "Score",cellFormat_gen));

        Resources resource = SiteSafetyApplication.getContext().getResources();
        ArrayList<String> elements = new ArrayList<>();
        elements.add(resource.getString(R.string.general_safety));
        elements.add(resource.getString(R.string.fire_safety));
        elements.add(resource.getString(R.string.electrical_safety));
        elements.add(resource.getString(R.string.emergency_prep));
        elements.add(resource.getString(R.string.workplace_safety));
        elements.add(resource.getString(R.string.safe_work));
        elements.add(resource.getString(R.string.vehicular_safety));
        elements.add(resource.getString(R.string.contractor_safety));
        elements.add(resource.getString(R.string.health_hygiene));
        elements.add(resource.getString(R.string.Safety_Signage));

//        ArrayList<GroupItem> survey = DbHelper.getSurveyDetails(surveyId);

//        if (survey == null) {
//            return;
//        }

        int totalYes = 0, totalNo = 0, totalNa = 0, totalPoints = 0;
        int totalYes_w = 0, totalNo_w = 0, totalNa_w = 0, totalPoints_w = 0;

        int i;
        for (i = 0; i < elements.size(); i++) {

            int no_w = 0, nan_w = 0, yes_w = 0, tp_w = 0, score;
            int no=0, nan=0 ,yes=0 , tp=0 ;
            for (GroupItem groupItem : groupItemsArrayList) {
                ArrayList<ChildItem> childItems = groupItem.getChildItems();
                for (ChildItem childItem : childItems) {
                    if (childItem.getElement().equals(elements.get(i))) {
                        switch (childItem.getResponse()) {
                            case -1:
                                no_w += childItem.getWeight();
                                no+=1;
                                tp+=1;
                                break;
                            case 0:
                                nan_w += childItem.getWeight();
                                nan+=1;
                                break;
                            case 1:
                                yes_w += childItem.getWeight();
                                yes+=1;
                                tp+=1;
                                break;
                        }
                    }
                }

            }

            // totalYes_w += yes_w;
            // totalNo_w += no_w;
            // totalNa_w += nan_w;
            // tp_w += (yes_w + no_w);
            // totalPoints_w += tp_w;
            // score = (int) (((float) yes_w / (yes_w + no_w)) * 100);
            totalYes +=yes;
            totalNo+= no;
            totalNa+=nan;
            totalPoints +=tp;
            score = (int) (((float) yes / (yes + no)) * 100);
            //   Log.e("AAAAA values", "NA :" + nan + " |Yes :" + yes + " |No :" + no + " |TP :" + (yes + no) + " |Score : " + ((float) yes / (yes + no)) * 100);

            WritableCellFormat cellFormat_scr_element = new WritableCellFormat();
            cellFormat_scr_element.setAlignment(Alignment.LEFT);
            cellFormat_scr_element.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);

            //           if (i==3){cellFormat_scr_element.setBackground(Colour.GREEN);}
            //           else if (i==4){cellFormat_scr_element.setBackground(Colour.ROSE);}
            //           else if(i==5){cellFormat_scr_element.setBackground(Colour.YELLOW);}


            sheet.addCell(new Label(1, i + 2, elements.get(i),cellFormat_scr_element));
            // sheet.addCell(new Label(2, i + 2, String.valueOf(nan)));
            // sheet.addCell(new Label(3, i + 2, String.valueOf(yes)));
            // sheet.addCell(new Label(4, i + 2, String.valueOf(no)));
            // sheet.addCell(new Label(5, i + 2, String.valueOf(tp)));
            // sheet.addCell(new Label(6, i + 2, score + "%"));
            sheet.addCell(new Number(2, i + 2, nan,cellFormat_gen));
            sheet.addCell(new Number(3, i + 2, yes,cellFormat_gen));
            sheet.addCell(new Number(4, i + 2, no,cellFormat_gen));
            sheet.addCell(new Number(5, i + 2,tp,cellFormat_gen));
            WritableCellFormat cellFormat_scr_element_score = new WritableCellFormat();
            cellFormat_scr_element_score.setAlignment(Alignment.RIGHT);
            cellFormat_scr_element_score.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);

            sheet.addCell(new Label(6, i + 2, score + "%",cellFormat_scr_element_score));


        }

        WritableCellFormat cellFormat_scr_total = new WritableCellFormat(font_scr_top);
        cellFormat_scr_total.setAlignment(Alignment.CENTRE);
        cellFormat_scr_total.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);

        //  sheet.addCell(new Label(1, i + 2, "TOTAL",cellFormat_scr_total));
        //  sheet.addCell(new Label(2, i + 2, String.valueOf(totalNa)));
        //  sheet.addCell(new Label(3, i + 2, String.valueOf(totalYes)));
        //  sheet.addCell(new Label(4, i + 2, String.valueOf(totalNo)));
        //  sheet.addCell(new Label(5, i + 2, String.valueOf(totalPoints)));
        //  sheet.addCell(new Label(6, i + 2, (int) (((float) totalYes / (totalYes + totalNo)) * 100) + "%"));

        sheet.addCell(new Number(2, i + 2, totalNa,cellFormat_gen));
        sheet.addCell(new Number(3, i + 2, totalYes,cellFormat_gen));
        sheet.addCell(new Number(4, i + 2, totalNo,cellFormat_gen));
        sheet.addCell(new Number(5, i + 2, totalPoints,cellFormat_gen));
//        WritableCellFormat cellFormat_scr_total_scr = new WritableCellFormat(font_scr_top);
//        cellFormat_scr_total_scr.setAlignment(Alignment.RIGHT);
//        cellFormat_scr_total_scr.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
//        sheet.addCell(new Label(6, i + 2, (int) (((float) totalYes / (totalYes + totalNo)) * 100) + "%",cellFormat_scr_total_scr));

        WritableCellFormat cellFormat_overall = new WritableCellFormat(font_scr_top);
        cellFormat_overall.setAlignment(Alignment.LEFT);
        cellFormat_overall.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
        cellFormat_overall.setBackground(Colour.RED);
        sheet.addCell(new Label(3, i + 5,"Overall Score  =",cellFormat_overall));
        sheet.mergeCells(3,i+5,4,i+5);

        WritableCellFormat cellFormat_overall_scr = new WritableCellFormat(font_scr_top);
        cellFormat_overall_scr.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
        cellFormat_overall_scr.setBackground(Colour.AQUA);
        sheet.addCell(new Number(5, i + 5, (int) (((float) totalYes / (totalYes + totalNo)) * 100),cellFormat_overall_scr));
*/
    }

}


