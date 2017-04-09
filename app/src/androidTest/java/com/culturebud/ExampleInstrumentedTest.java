package com.culturebud;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.reflect.TypeToken;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.culturebud.bean.ApiResultBean;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.BookCircleDynamicRelationMe;
import com.culturebud.bean.BookCommunityDetail;
import com.culturebud.bean.BookDetail;
import com.culturebud.bean.BookSheetDetail;
import com.culturebud.bean.CommentReply;
import com.culturebud.net.ApiBookHomeInterface;
import com.culturebud.net.ApiBookInterface;
import com.culturebud.net.ApiBookSheetInterface;
import com.culturebud.net.ApiCommunityInterface;
import com.culturebud.net.ApiHomeInterface;
import com.culturebud.net.ApiFileInterface;
import com.culturebud.util.DigestUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;

import static com.culturebud.CommonConst.DEVICE_TOKEN;
import static com.culturebud.CommonConst.DEVICE_TOKEN_KEY;
import static com.culturebud.CommonConst.PLATFORM;
import static com.culturebud.CommonConst.PLATFORM_KEY;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "ExampleInstrumentedTest";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.culturebud", appContext.getPackageName());
    }

    @Test
    public void testNet() {
        Retrofit retrofit = initRetrofit(null);

        ApiHomeInterface home = retrofit.create(ApiHomeInterface.class);
        home.getFrontPageList(new HashMap<>())
                .subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResultBean<JsonObject> bean) {

                    }
                });
    }

    private static final String TOKEN = "86a2791b-e236-4bdd-8b87-94fe1aec10bc";

    @Test
    public void testComments() {
        Retrofit retrofit = initRetrofit(null);
        ApiCommunityInterface community = retrofit.create(ApiCommunityInterface.class);
        Map<String, Object> params = new HashMap<>();
        params.put("page", 0);
        params.put("token", TOKEN);
        community.comment(params).enqueue(new Callback<ApiResultBean<JsonObject>>() {
            @Override
            public void onResponse(Call<ApiResultBean<JsonObject>> call, Response<ApiResultBean<JsonObject>> response) {
                Log.e(TAG, response.body().toString());
            }

            @Override
            public void onFailure(Call<ApiResultBean<JsonObject>> call, Throwable t) {
                Log.e(TAG, "onFailure");
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Test
    public void testOrginNet() throws IOException {
        URL url = new URL(CommonConst.API_HOST + "api/common/home");

        URLConnection conn = url.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[4096];
        bis.read(buffer, 0, 4096);
        Log.e(TAG, new String(buffer));
    }

    @Test
    public void testPwdMd5() {
        Log.e(TAG, DigestUtil.md5("xiewei123"));
    }

    @Test
    public void testOrginLogin() throws Exception {
        URL url = new URL(CommonConst.API_HOST + CommonConst.PATH_LOGIN);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        DataOutputStream out = new DataOutputStream(conn
                .getOutputStream());
        // The URL-encoded contend
        // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
        String content = "platform=" + URLEncoder.encode("Android", "UTF-8");
        content += "&deviceToken=" + URLEncoder.encode(BaseApp.getInstance().getDeviceId(), "UTF-8");
        content += "&userName=" + URLEncoder.encode("15071031800", "UTF-8");
        content += "&password=" + URLEncoder.encode(DigestUtil.md5("xw123456"), "UTF-8");
        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
        out.writeBytes(content);

        out.flush();
        out.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        reader.close();
        conn.disconnect();
    }

    @Test
    public void testOrginBookCircleDynamic() throws Exception {
        URL url = new URL(CommonConst.API_HOST + CommonConst.PATH_BOOK_CIRCLE_DYNAMIC);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        DataOutputStream out = new DataOutputStream(conn
                .getOutputStream());
        // The URL-encoded contend
        // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
        String content = "platform=" + URLEncoder.encode("Android", "UTF-8");
        content += "&deviceToken=" + URLEncoder.encode(BaseApp.getInstance().getDeviceId(), "UTF-8");
        content += "&token=86a2791b-e236-4bdd-8b87-94fe1aec10bc";
        content += "&page=0";
        content += "&userId=100009";
        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
        out.writeBytes(content);

        out.flush();
        out.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        char[] buffer = new char[1024];
        int len = 0;
        while ((len = reader.read(buffer, 0, 1024)) != -1) {
            Log.d("xwlljj", new String(buffer, 0, len));
        }

        reader.close();
        conn.disconnect();
    }

    @Test
    public void testThumbUp() {
        Retrofit retrofit = initRetrofit(null);

        ApiCommunityInterface communityInterface = retrofit.create(ApiCommunityInterface.class);
        Map<String, Object> params = new HashMap<>();
        params.put("platform", "Android");
        params.put("deviceToken", BaseApp.getInstance().getDeviceId());
        params.put("token", "718f6f93-d4b8-4be9-a2b6-bae8aaf6bf44");
        params.put("goodType", 0);
        params.put("goodObjId", 8);

        communityInterface.thumbUp(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onNext(ApiResultBean<JsonObject> bean) {
                Log.e(TAG, bean.toString());
            }
        });
    }

    @NonNull
    private Retrofit initRetrofit(String url) {
        return new Retrofit.Builder()
                .baseUrl(CommonConst.API_HOST)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Test
    public void testDownloadFile() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.mywengu.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ;
        ApiFileInterface download = retrofit.create(ApiFileInterface.class);
        String url = "/img/user/background/lpic/default/default.jpg";
        Log.e("xwlljj", BaseApp.getInstance().getFilesDir().getPath() + Uri.parse(url).getPath());
//        Call<ResponseBody> call = download.downloadFile("");
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.e("xwlljj", "onCall");
//                ResponseBody responseBody = response.body();
//                Uri uri = Uri.parse(url);
//                String path = uri.getPath();
//                String dir = BaseApp.getInstance().getFilesDir().getPath();
//                try {
//                    Log.e("xwlljj", dir + path);
//                    FileOutputStream fis = new FileOutputStream(dir + path);
//                    fis.write(responseBody.bytes());
//                    Bitmap bitmap = BitmapFactory.decodeFile(dir + path);
//                    if (bitmap != null && !bitmap.isRecycled()) {
//                        Log.e("xwlljj", "bitmap download success");
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("xwlljj", "onFailure:" + t.getMessage());
//            }
//        });
    }

    @Test
    public void testBooks() {
        Retrofit retrofit = initRetrofit(null);
        ApiCommunityInterface communityInterface = retrofit.create(ApiCommunityInterface.class);
        Map<String, Object> params = new HashMap<>();
        params.put("platform", "Android");
        params.put("deviceToken", BaseApp.getInstance().getDeviceId());
        params.put("token", "2d4772d9-63a5-4c83-9111-7bbb1c7375fb");
        params.put("page", 0);
        params.put("communityId", 314013);
        Call<ApiResultBean<JsonObject>> call = communityInterface.books(params);
        call.enqueue(new Callback<ApiResultBean<JsonObject>>() {
            @Override
            public void onResponse(Call<ApiResultBean<JsonObject>> call, Response<ApiResultBean<JsonObject>> response) {
                Log.e("xwlljj", response.body().toString());
                JsonArray jsonArray = response.body().getData().getAsJsonArray("bookList");
                Gson gson = new Gson();
                Type type = new TypeToken<List<Book>>() {
                }.getType();
                List<Book> books = gson.fromJson(jsonArray, type);
                for (Book book : books) {
                    Log.e("xwlljj", book.toString());
                }
            }

            @Override
            public void onFailure(Call<ApiResultBean<JsonObject>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Test
    public void testGetBooks() {
        Retrofit retrofit = initRetrofit(null);
        Map<String, Object> params = new HashMap<>();
        params.put(PLATFORM_KEY, PLATFORM);
        params.put(DEVICE_TOKEN_KEY, DEVICE_TOKEN);
        params.put("token", "2d4772d9-63a5-4c83-9111-7bbb1c7375fb");
        params.put("page", 0);
        params.put("sortType", 0);
//        params.put("filterType", "");
        retrofit.create(ApiBookInterface.class).getBooks(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ApiResultBean<JsonObject> jsonObjectApiResultBean) {

            }
        });
    }

    @Test
    public void testSearchBook() {
        ApiBookInterface abi = initRetrofit(null).create(ApiBookInterface.class);
        Map<String, Object> params = new HashMap<>();
        params.put(PLATFORM_KEY, PLATFORM);
        params.put(DEVICE_TOKEN_KEY, DEVICE_TOKEN);
        params.put("page", 0);
        params.put("keyword", "wo");
        Log.e("xwlljj", "start-->");
        abi.searchBooks(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
            @Override
            public void onCompleted() {
                Log.e("xwlljj", "onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("xwlljj", "onError()");
                e.printStackTrace();
            }

            @Override
            public void onNext(ApiResultBean<JsonObject> bean) {
                Log.e("xwlljj", bean.toString());
                int code = bean.getCode();
                if (code == 200) {
                    JsonObject jobj = bean.getData();
                    if (jobj.has("bookList")) {
                        JsonArray jarr = jobj.getAsJsonArray("bookList");
                        Gson gson = new Gson();
                        List<Book> books = gson.fromJson(jarr, new TypeToken<List<Book>>() {
                        }.getType());
                        Log.e("xwlljj", books.toString());
                    }
                }
            }
        });
    }

    @Test
    public void testBookSheetDetail() {
        ApiBookSheetInterface bs = initRetrofit(null).create(ApiBookSheetInterface.class);
        Map<String, Object> params = new HashMap<>();
        params.put(PLATFORM_KEY, PLATFORM);
        params.put(DEVICE_TOKEN_KEY, DEVICE_TOKEN);
        params.put("token", "706938b1-bcd2-4346-900e-63859909fb7e");
        params.put("sheetId", 22);
        bs.getBookSheetDetail(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ApiResultBean<JsonObject> bean) {
                Log.e("xwlljj", bean.getData().toString());
                JsonObject jobj = bean.getData();
                jobj = jobj.getAsJsonObject("bookSheetDetail");
                Gson gson = new Gson();
                BookSheetDetail detail = gson.fromJson(jobj, new TypeToken<BookSheetDetail>() {
                }.getType());
                Log.e("xwlljj", detail.toString());
            }
        });
    }

    @Test
    public void testCommentReplies() {
        ApiCommunityInterface ci = initRetrofit(null).create(ApiCommunityInterface.class);
        Map<String, Object> params = new HashMap<>();
        params.put(PLATFORM_KEY, PLATFORM);
        params.put(DEVICE_TOKEN_KEY, DEVICE_TOKEN);
        params.put("token", "706938b1-bcd2-4346-900e-63859909fb7e");
        params.put("commentId", 22);
        params.put("page", 0);
        ci.commentReplies(params).subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ApiResultBean<JsonObject> bean) {
                if (bean.getCode() == 200) {
                    JsonObject jobj = bean.getData();
                    if (jobj.has("commentReplyList")) {
                        JsonArray jarr = jobj.getAsJsonArray("commentReplyList");
                        Gson gson = new Gson();
                        List<CommentReply> replies = gson.fromJson(jarr, new TypeToken<List<CommentReply>>() {
                        }.getType());

                        for (CommentReply cr : replies) {
                            Log.e("xwlljj", cr.toString());
                        }
                    }
                    Log.e("xwlljj", jobj.toString());
                } else {

                }

            }
        });
    }

    @Test
    public void testCommunityDetail() {
        ApiCommunityInterface comm = initRetrofit(null).create(ApiCommunityInterface.class);
        Map<String, Object> params = new HashMap<>();
        params.put(PLATFORM_KEY, PLATFORM);
        params.put(DEVICE_TOKEN_KEY, DEVICE_TOKEN);
        params.put("token", "b00fd609-17a2-480b-9568-1f70af393344");
        params.put("communityId", 314013);
        comm.communityDetail(params).subscribe(new Subscriber<ApiResultBean<BookCommunityDetail>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ApiResultBean<BookCommunityDetail> bean) {
                Log.e("xwlljj", bean.getData().toString());
            }
        });
    }

    @Test
    public void testSwitch() {
        int v = 5;

        switch (v) {
            case 0:
                Log.d(TAG, "0");
            case 1:
                Log.d(TAG, "1");
            case 2:
                Log.d(TAG, "2");
            case 3:
                Log.d(TAG, "3");
            case 4:
                Log.d(TAG, "4");
            case 5:
                Log.d(TAG, "5");
            case 6:
                Log.d(TAG, "6");
            case 7:
                Log.d(TAG, "7");
        }
    }

    @Test
    public void testGson() {
        String json = "{\"author\":\"王澍\",\"authorInfo\":\"王澍，中国美术学院教授，东南大学、同济大学兼职教授，哈佛大学研究生院丹下健三讲席教授，香港大学和麻省理工学院客座教授。1997年与他的妻子陆文宇成立业余建筑工作室，致力于重新构筑中国当代建筑的研究和工作，并体现在他的作品宁波博物馆、宁波美术馆、中国美术学院象山校区（位 于杭州转塘）、上海世博会宁波滕头馆、垂直院宅（杭州钱江时代）、杭州南宋御街综合保护与改造等一系列作品中。2010年，和陆文宇获德国谢林建筑实践大奖及威尼斯双年展特别荣誉奖。2011年，获法国建筑科学院金奖。2012年，获普利兹克建筑奖。2012年，获《华尔街日报》评选的“全球创新人物奖”。2013年，被美国《时代》周刊评为全球百大最具影响力人物。\",\"binding\":\"精装\",\"bookId\":309524,\"collect\":false,\"collectionNum\":0,\"communityId\":359362,\"cover\":\"http://www.mywengu.com/img/book/cover/lpic/30/309524_306x435.jpg\",\"isbn10\":\"7535678092\",\"isbn13\":\"9787535678096\",\"pages\":\"\",\"price\":\"78.00元\",\"pubDate\":\"2016-8\",\"publisher\":\"浦睿文化／湖南美术出版社\",\"rating\":9.1,\"remarkNum\":0,\"subTitle\":\"\",\"summary\":\"本书是世界建筑最高奖普利兹克奖得主、著名建筑大师王澍的建筑文化随笔集。本书从建筑出发，却不止于建筑，更是一本探讨中国传统文化当代性的著作。传统文化的当代性一直是这些年学界反复思索和讨论的重要课题，王澍以自己的学术素养，以及营造经验，构建出独特的关于东方美学的审美体系，也给出传统文化进入当代的路径，这对于当下有非常重要的学术参考价值。\\n10篇建筑文化随笔——从宋代山水画的意境，到明清园林的审美情趣，作者深入剖析中国传统文化、艺术，更以建筑的角度，从中探寻传统文化、东方哲学的美学价值。\\n4篇建筑作品历程书写——王澍的著名建筑作品 包括中国美院象山校区、宁波美术馆等，在本书中，从设计开端、建造过程，直至建成后续，作者用深入浅出的语言，还原这些作品的诞生历程。从中，我们看到的是作者对于“好的建筑”以及“如何做出重返传统的当代建筑”的深入思考。\\n6篇散文随笔+1篇对谈——作者漫谈个人经历、社会与人生，更触及当下人关心的居住空间等话题，大师的成长历程和人文情怀一览无遗。\",\"tag\":\"建筑|王澍|设计|艺术|中国|传统文化|随笔|W王澍\",\"title\":\"造房子\",\"translator\":\"\",\"updatedTime\":1477541959000}";
        BookDetail bookDetail = new Gson().fromJson(json, BookDetail.class);
        Log.e("xwlljj", bookDetail.toString());
//        BookDetail bd = new BookDetail();
//        bd.setAuthor("王澍");
//        bd.setAuthorInfo("王澍，中国美术学院教授，东南大学、同济大学兼职教授，哈佛大学研究生院丹下健三讲席教授，香港大学和麻省理工学院客座教授。1997年与他的妻子陆文宇成立业余建筑工作室，致力于重新构筑中国当代建筑的研究和工作，并体现在他的作品宁波博物馆、宁波美术馆、中国美术学院象山校区（位 于杭州转塘）、上海世博会宁波滕头馆、垂直院宅（杭州钱江时代）、杭州南宋御街综合保护与改造等一系列作品中。2010年，和陆文宇获德国谢林建筑实践大奖及威尼斯双年展特别荣誉奖。2011年，获法国建筑科学院金奖。2012年，获普利兹克建筑奖。2012年，获《华尔街日报》评选的“全球创新人物奖”。2013年，被美国《时代》周刊评为全球百大最具影响力人物。");
//        bd.setBinding("精装");
//        bd.setBookId(309524);
//        bd.setCollect(false);
//        bd.setCollectionNum(0);
//        bd.setCommunityId(359362);
//        bd.setCover("http://www.mywengu.com/img/book/cover/lpic/30/309524_306x435.jpg");
//        bd.setIsbn10("7535678092");
//        bd.setIsbn13("9787535678096");
//        bd.setPages();
//        bd.setPrice("78.00元");
//        bd.setPubDate("2016-8");
//        bd.setPublisher("浦睿文化／湖南美术出版社");
//        bd.setRating(9.1F);
//        bd.setRemarkNum(0);
//        bd.setSubTitle("");
//        bd.setSummary("本书是世界建筑最高奖普利兹克奖得主、著名建筑大师王澍的建筑文化随笔集。本书从建筑出发，却不止于建筑，更是一本探讨中国传统文化当代性的著作。传统文化的当代性一直是这些年学界反复思索和讨论的重要课题，王澍以自己的学术素养，以及营造经验，构建出独特的关于东方美学的审美体系，也给出传统文化进入当代的路径，这对于当下有非常重要的学术参考价值。\\n10篇建筑文化随笔——从宋代山水画的意境，到明清园林的审美情趣，作者深入剖析中国传统文化、艺术，更以建筑的角度，从中探寻传统文化、东方哲学的美学价值。\\n4篇建筑作品历程书写——王澍的著名建筑作品 包括中国美院象山校区、宁波美术馆等，在本书中，从设计开端、建造过程，直至建成后续，作者用深入浅出的语言，还原这些作品的诞生历程。从中，我们看到的是作者对于“好的建筑”以及“如何做出重返传统的当代建筑”的深入思考。\\n6篇散文随笔+1篇对谈——作者漫谈个人经历、社会与人生，更触及当下人关心的居住空间等话题，大师的成长历程和人文情怀一览无遗。");
//        bd.setTag("建筑|王澍|设计|艺术|中国|传统文化|随笔|W王澍");
//        bd.setTitle("造房子");
//        bd.setTranslator("");
//        bd.setUpdatedTime(1477541959000L);
//        Log.e("xwlljj", new Gson().toJson(bd));
    }

    @Test
    public void testPinyin4j() {
        char[] tmp = {'w', '谢', '&'};
        Log.d(TAG, "a = " + ((int) 'a'));
        Log.d(TAG, "z = " + ((int) 'z'));
        Log.d(TAG, "A = " + ((int) 'A'));
        Log.d(TAG, "Z = " + ((int) 'Z'));
        for (char c : tmp) {
            String[] arr = PinyinHelper.toHanyuPinyinStringArray(c);
            if (arr != null && arr.length > 0) {
                Log.d(TAG, arr[0]);
            } else {
                Log.d(TAG, "*******");
            }
        }
    }

    @Test
    public void testCommon() {
        //af8f9f1a-ee36-4a99-9407-8315dd306f7c
        Map<String, Object> params = new HashMap<>();
//        params.put("token", "718f6f93-d4b8-4be9-a2b6-bae8aaf6bf44");
//        params.put("page", 0);
        params.put(PLATFORM_KEY, PLATFORM);
        params.put(DEVICE_TOKEN_KEY, DEVICE_TOKEN);
        initRetrofit(null).create(ApiBookSheetInterface.class).getTags(params)
        .subscribe(new Subscriber<ApiResultBean<JsonObject>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ApiResultBean<JsonObject> bean) {
                if (bean.getCode() == ApiErrorCode.CODE_SUCCESS) {
                    Log.d(TAG, bean.getData().toString());
                } else {
                    Log.d(TAG, bean.toString());
                }
            }
        });
    }


}
