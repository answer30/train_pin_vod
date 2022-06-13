package com.pin.train_pin_vod;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

//import com.motrex.smartapp.db.AudioUserList_DB;


/** MOTREX CONFIDENTIAL
 *
 *  _____________________________________________________________________
 *
 *  [2017] - [2027] MOTREX
 *  All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains
 *  the property of MOTREX and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to MOTREX and its suppliers and
 *  may be covered by Korea and Foreign Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from MOTREX.
 *
 *  */



public class DaoFile {
	public static final int ALL_VOD 				= 0;
	public static final int CAMERA_VOD 				= 1;

	public static final String AUDIO_EXTERNAL= "AUDIO_EXTERNAL";
	public static final String VIDEO_EXTERNAL= "VIDEO_EXTERNAL";
	public static final String VIDEO_EXTERNAL_PLAYER= "VIDEO_EXTERNAL_PLAYER";

	public Context con;
	public DaoFile(Context con_){
		con = con_;
	}
	//占쏙옙占쏙옙占�
	String projectionAudio[] =  new String[] {
		MediaStore.Audio.Media._ID,
		MediaStore.Audio.Media.TITLE,
		MediaStore.Audio.Media.TITLE_KEY,
		MediaStore.Audio.Media.DATA,
		MediaStore.Audio.Media.ALBUM,
		MediaStore.Audio.Media.ALBUM_ID,
		MediaStore.Audio.Media.ARTIST,
		MediaStore.Audio.Media.ARTIST_ID,
		MediaStore.Audio.Media.DURATION,
		MediaStore.Audio.Media.MIME_TYPE,
	};


	String projectionVideo[] =  new String[] {
		MediaStore.Video.Media._ID,
		MediaStore.Video.Media.TITLE,
		MediaStore.Video.Media.DATA,
		MediaStore.Video.Media.ALBUM,
		MediaStore.Video.Media.ARTIST,
		MediaStore.Video.Media.DURATION,
		MediaStore.Video.Media.MIME_TYPE,
		MediaStore.Video.Media.SIZE,
		MediaStore.Video.Media.BOOKMARK,
	};

	public Cursor getQueryAudioList(String state){
		if(state.equals(AUDIO_EXTERNAL)){
			return getQueryAudioList(state , null);
			//    		MediaStore.Audio.Media.IS_MUSIC + "=1"
		}else{
			return null;
		}
	}


	//    String supportFormat_audio = MediaStore.Audio.Media.DATA+" LIKE '%.wav%' or " + MediaStore.Audio.Media.DATA+" LIKE '%.ogg%' or " +
	//	  MediaStore.Audio.Media.DATA+" LIKE '%.3gp%' or "+MediaStore.Audio.Media.DATA+" LIKE '%.mp3%'";

//	String supportFormat_audio = MediaStore.Audio.Media.DATA+" LIKE '%.wav%' or " +
//		MediaStore.Audio.Media.DATA+" LIKE '%.3gp%' or "+MediaStore.Audio.Media.DATA+" LIKE '%.mp3%'";

	String supportFormat_audio =  MediaStore.Audio.Media.DATA+" LIKE '%.mp3%'";

	String supportFormat_video = MediaStore.Video.Media.DATA+" LIKE '%.3gp%' or "+ MediaStore.Video.Media.DATA+" LIKE '%.mp4%'";

	public Cursor getQueryAudioList(String state , String selection){

		if(selection == null){
			selection = supportFormat_audio;
		}else{
			selection = "("+supportFormat_audio + ") and " +selection;
		}

		if(state.equals(AUDIO_EXTERNAL)){
			return con.getContentResolver().query( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				projectionAudio, 	selection, null,  null);
		}else{
			return null;
		}
	}

	/**
	 * @param state
	 * @return
	 */
	public Cursor getQueryVideoList(String state){
		if(state.equals(VIDEO_EXTERNAL)){
			return getQueryVideoList(state , null);
		}else{
			return null;
		}
	}
	public Cursor getQueryVideoList(String state , String selection){

		//    	if(selection == null){
		//    		selection = supportFormat_video;
		//    	}else{
		////<<<<<<< Dao_file.java
		//    		selection = "(" + supportFormat_video + ") and " +selection;
		////=======
		//    		selection = "("+supportFormat_video + ") and " +selection;
		////>>>>>>> 1.3
		//    	}

		if(VIDEO_EXTERNAL_PLAYER.equals(state)){
			return con.getContentResolver().query( MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				projectionVideo, "(" + supportFormat_video + ") and " +selection, null, null);
		}else if(state.equals(VIDEO_EXTERNAL)){
			return con.getContentResolver().query( MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				projectionVideo, selection, null, null);
		}else{
			return null;
		}
	}

	private String pathCheck(String path) {
		int checkIndex = path.lastIndexOf("'");
		if(checkIndex > 0) {
			StringBuffer tempPath = new StringBuffer();
			tempPath.append(path.substring(0, checkIndex));
			tempPath.append("''");
			tempPath.append(path.substring(checkIndex+1, path.length()));
			return tempPath.toString();
		}

		return path;
	}

	/**
	 * 占싱듸옙占� 占쏙옙占싸뱄옙占싱댐옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
	 *
	 * 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쌕뤄옙 占싣뤄옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 , 占쏙옙占쏙옙占쏙옙占쏙옙  占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙
	 * @param path
	 * @param folderName
	 * @return
	 */
	public ArrayList<String[]> getAudioSelectList(String path, String folderName){
		ArrayList<String[]> folderList = new ArrayList<String[]>();
		String where = pathCheck(path);
		Cursor aContentListCusor = getQueryAudioList(DaoFile.AUDIO_EXTERNAL, MediaStore.Audio.Media.DATA+" LIKE '%"+where+"%'");

		for(int i = 0 ; i < aContentListCusor.getCount() ; i++){
			aContentListCusor.moveToPosition(i);

			String[] cData = new String[6];//0 : DATA (PATH)		1:TITLE				2:ARTIST			3:占쏙옙占쏙옙챨占�			4:占쌕뱄옙占쏙옙占싱듸옙 (占쌕뱄옙占싱뱄옙占쏙옙 占쌜억옙占쏙옙占쏙옙 )   5:ID

			cData[0] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.DATA));
			if(folderName.equals(new File(cData[0]).getParentFile().getName())){
				cData[1] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.TITLE));
				cData[2] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				cData[3] = ""+aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.DURATION));
				cData[4] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
				cData[5] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Audio.Media._ID)));

				folderList.add(cData);
			}
		}

		aContentListCusor.close();

		return folderList;
	}

	public ArrayList<String[]> getAudioAllList(){
		ArrayList<String[]> audioList = new ArrayList<String[]>();
		Cursor aContentListCusor = getQueryAudioList(DaoFile.AUDIO_EXTERNAL, null);

		for(int i = 0 ; i < aContentListCusor.getCount() ; i++){
			aContentListCusor.moveToPosition(i);

			String[] cData = new String[7];//0 : DATA (PATH)		1:TITLE		2:ARTIST	3:占쏙옙占쏙옙챨占�	4:占쌕뱄옙占쏙옙占싱듸옙 (占쌕뱄옙占싱뱄옙占쏙옙 占쌜억옙占쏙옙占쏙옙 )   5:ID

			cData[0] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.DATA));
			cData[1] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.TITLE));
			cData[2] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			cData[3] = ""+aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.DURATION));
			cData[4] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
			cData[5] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Audio.Media._ID)));

			audioList.add(cData);
		}

		aContentListCusor.close();

		return audioList;
	}


	/**
	 * 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싼댐옙
	 * @param path
	 * @return
	 */
	public String[] getAudioSelectContent(String path){
		String where = pathCheck(path);
		Cursor aContentListCusor = getQueryAudioList(DaoFile.AUDIO_EXTERNAL, MediaStore.Audio.Media.DATA+" LIKE '%"+where+"%'");
		aContentListCusor.moveToPosition(0);

		String[] cData = new String[6];//0 : DATA (PATH)		1:TITLE				2:ARTIST			3:占쏙옙占쏙옙챨占�			4:占쌕뱄옙占쏙옙占싱듸옙 (占쌕뱄옙占싱뱄옙占쏙옙 占쌜억옙占쏙옙占쏙옙 )   5:ID
		cData[0] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.DATA));
		cData[1] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.TITLE));
		cData[2] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
		cData[3] = ""+aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.DURATION));
		cData[4] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
		cData[5] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Audio.Media._ID)));

		aContentListCusor.close();
		return cData;
	}

	public boolean isAudioContentExist(String path){
		String where = pathCheck(path);
		Cursor aContentListCusor = getQueryAudioList(DaoFile.AUDIO_EXTERNAL, MediaStore.Audio.Media.DATA+" LIKE '%"+where+"%'");

		if(aContentListCusor != null && aContentListCusor.getCount() != 0){
			aContentListCusor.close();
			return true;
		}

		return false;
	}

//	/**
//	 *  AudioUserList_DB 占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
//	 *
//	 *   占쏙옙占쏙옙占� 占쏙옙占쏙옙 占쏙옙占쏙옙트 占쏙옙占쏙옙
//	 *   ArrayList<String[]> 占쏙옙占승뤄옙 占쏙옙占쏙옙
//	 * @param aContentListCusor
//	 * @return
//	 */
//	public ArrayList<String[]> getAudioUserSelectList(Cursor aContentListCusor ,AudioUserList_DB userDB){
//		ArrayList<String[]>  folderList = new ArrayList<String[]> ();
//		for(int i = 0 ; i < aContentListCusor.getCount() ; i++){
//			aContentListCusor.moveToPosition(i);
//
//			String[] cData = new String[7];//0 : DATA (PATH)		1:TITLE				2:ARTIST			3:占쏙옙占쏙옙챨占�			4:占쌕뱄옙占쏙옙占싱듸옙 (占쌕뱄옙占싱뱄옙占쏙옙 占쌜억옙占쏙옙占쏙옙 )   5:占싱듸옙占폠D  6;db id
//
//			cData[0] = aContentListCusor.getString(aContentListCusor.getColumnIndex(AudioUserList_DB.DATA));
//			cData[5] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(AudioUserList_DB.MEDIA_ID)));
//			cData[6] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(AudioUserList_DB._ID)));
//			if(new File(cData[0]).exists()){//占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙荑∽옙占� 占쏙옙占쏙옙
//				cData[1] = aContentListCusor.getString(aContentListCusor.getColumnIndex(AudioUserList_DB.TITLE));
//				cData[2] = aContentListCusor.getString(aContentListCusor.getColumnIndex(AudioUserList_DB.ARTIST));
//				cData[3] = ""+aContentListCusor.getLong(aContentListCusor.getColumnIndex(AudioUserList_DB.DURATION));
//				cData[4] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(AudioUserList_DB.ALBUM_ID)));
//
//				folderList.add(cData);
//			}else{//占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占�
//				if(userDB != null){//占쏙옙占쏙옙占� DB占쏙옙占쏙옙 占쏙옙占쏙옙
//					userDB.delete_DBData(cData[6]);
//				}
//			}
//		}
//
//		aContentListCusor.close();
//
//		return folderList;
//	}

	public ArrayList<String[]> getVideoSelectList(String path, String folderName){
		ArrayList<String[]> folderList = new ArrayList<String[]>();
		String where = pathCheck(path);
		Cursor aContentListCusor = getQueryVideoList(VIDEO_EXTERNAL, MediaStore.Video.Media.DATA+" LIKE '%"+where+"%'");

		for(int i = 0 ; i < aContentListCusor.getCount() ; i++){
			aContentListCusor.moveToPosition(i);

			String[] cData = new String[6];//0 : DATA (PATH)		1:TITLE				2:BOOKMARK			3:占쏙옙占쏙옙챨占�			4:size     5:ID


			cData[0] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Video.Media.DATA));
			if(folderName.equals(new File(cData[0]).getParentFile().getName())){
				cData[1] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Video.Media.TITLE));
				cData[2] = ""+aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media.BOOKMARK));
				cData[3] = ""+aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media.DURATION));
				cData[4] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media.SIZE)));
				cData[5] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media._ID)));
				folderList.add(cData);
			}
		}

		aContentListCusor.close();

		return folderList;
	}

	public ArrayList<String[]> getVideoPlayerList(String path, String folderName){
		ArrayList<String[]> folderList = new ArrayList<String[]>();
		String where = pathCheck(path);
		Cursor aContentListCusor = getQueryVideoList(VIDEO_EXTERNAL_PLAYER, MediaStore.Video.Media.DATA+" LIKE '%"+where+"%'");

		for(int i = 0 ; i < aContentListCusor.getCount() ; i++){
			aContentListCusor.moveToPosition(i);

			String[] cData = new String[6];//0 : DATA (PATH)		1:TITLE				2:BOOKMARK			3:占쏙옙占쏙옙챨占�			4:size     5:ID


			cData[0] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Video.Media.DATA));
			if(folderName.equals(new File(cData[0]).getParentFile().getName())){
				cData[1] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Video.Media.TITLE));
				cData[2] = ""+aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media.BOOKMARK));
				cData[3] = ""+aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media.DURATION));
				cData[4] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media.SIZE)));
				cData[5] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media._ID)));
				folderList.add(cData);
			}
		}

		aContentListCusor.close();

		return folderList;
	}

	public ArrayList<String[]> getVideoAllList(){
		ArrayList<String[]> folderList = new ArrayList<String[]>();
		Cursor aContentListCusor = getQueryVideoList(VIDEO_EXTERNAL,null);

		for(int i = 0 ; i < aContentListCusor.getCount() ; i++){
			aContentListCusor.moveToPosition(i);

			String[] cData = new String[6];//0 : DATA (PATH)		1:TITLE				2:BOOKMARK			3:占쏙옙占쏙옙챨占�			4:size     5:ID

			cData[0] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Video.Media.DATA));
			cData[1] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Video.Media.TITLE));
			cData[2] = ""+aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media.BOOKMARK));
			cData[3] = ""+aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media.DURATION));
			cData[4] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media.SIZE)));
			cData[5] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media._ID)));
			folderList.add(cData);
		}

		aContentListCusor.close();

		Log.d("JJJ", "getVideoAllList: " + folderList.size());

		return folderList;
	}

	/**
	 * 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싼댐옙
	 * @param path
	 * @return
	 */
	public String[] getVideoSelectContent(String path){
		String where = pathCheck(path);
		Cursor aContentListCusor = getQueryVideoList(DaoFile.VIDEO_EXTERNAL, MediaStore.Video.Media.DATA+" LIKE '%"+where+"%'");
		aContentListCusor.moveToPosition(0);


		String[] cData = new String[6];//0 : DATA (PATH)		1:TITLE				2:BOOKMARK			3:占쏙옙占쏙옙챨占�			4:size     5:ID
		cData[0] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Video.Media.DATA));
		cData[1] = aContentListCusor.getString(aContentListCusor.getColumnIndex(MediaStore.Video.Media.TITLE));
		cData[2] = ""+aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media.BOOKMARK));
		cData[3] = ""+aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media.DURATION));
		cData[4] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media.SIZE)));
		cData[5] = String.valueOf(aContentListCusor.getLong(aContentListCusor.getColumnIndex(MediaStore.Video.Media._ID)));

		aContentListCusor.close();
		return cData;
	}

	public boolean isVideoContentExist(String path){
		Cursor aContentListCusor = getQueryVideoList(DaoFile.VIDEO_EXTERNAL, MediaStore.Video.Media.DATA+" LIKE '%"+path+"%'");

		if(aContentListCusor != null && aContentListCusor.getCount() != 0){
			aContentListCusor.close();
			return true;
		}

		return false;
	}

	/**
	 * 占싱어보占쏙옙 占시곤옙 占쏙옙占쏙옙
	 * @param columnID
	 * @param saveTime
	 */
	public void saveVideoContinuePlay(String columnID , int saveTime) {
		ContentValues values = new ContentValues();
		Uri uri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columnID);
		values.put(MediaStore.Video.Media.BOOKMARK, saveTime);

		con.getContentResolver().update(uri, values, null, null);
	}



//	/* USB Media List */
//
//	public ArrayList<String[]> getUsbMediaMusicList(MediaFileList list) {
//
//		ArrayList<String[]> mp3List = new ArrayList<String[]>();
////
////		String mp3Path = Environment.getExternalStorageDirectory() + "/Media";
//
//		ArrayList<MediaFileList.MediaFileInfo> mFileList = list.getFileList();
//
////		File[] listFiles = new File(mp3Path).listFiles();
//		String fileName, extName;
//
////		Log.i("getUsbMediaMusicList", "mp3Path : " + mp3Path);
//
//		for (MediaFileList.MediaFileInfo file : mFileList) {
//			fileName = file.getFile().getName();
//			extName = fileName.substring(fileName.length() - 3);
//
//			Log.i("getUsbMediaMusicList", "fileName : " + fileName);
//
//			if (extName.equals("mp3")) {
//
//				Mp3File song = null;
//				try {
//					song = new Mp3File(file.getFile().getPath() + "/" + fileName);
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (UnsupportedTagException e) {
//					e.printStackTrace();
//				} catch (InvalidDataException e) {
//					e.printStackTrace();
//				}
//
//				if(song.hasId3v2Tag()) {
//					ID3v2 id3v2tag = song.getId3v2Tag();
//
//					String[] cData = new String[7]; //0 : DATA (PATH)		1:TITLE		2:ARTIST	3:LENGTH    4:IMAGE    5:ISFAVORITE
//
//					if(id3v2tag.getAlbumImage().length == 0) {
//						Log.i("getUsbMediaMusicList", id3v2tag.getTitle() + "'s album image is null.");
//					}
//
//					String image = null;
//					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
//						image = Base64.encodeToString(id3v2tag.getAlbumImage(), Base64.DEFAULT);
//					}
//
//					cData[0] = file.getFile().getPath() + "/" + fileName;
//					cData[1] = id3v2tag.getTitle();
//					cData[2] = id3v2tag.getArtist();
//					cData[3] = String.valueOf(id3v2tag.getDataLength());
//					cData[4] = String.valueOf(image);
//					cData[5] = "false";
//
//					for(int i=0; i<5; i++)
//					 Log.i("getUsbMediaMusicList", cData[i]);
//
//					mp3List.add(cData);
//
//				}
//			}
//		}
//
//		return mp3List;
//	}

	public ArrayList<String[]> getUsbMediaMovieList() {

		ArrayList<String[]> mp4List = new ArrayList<String[]>();

		String mp4Path = Environment.getExternalStorageDirectory() + "/Media";
		File[] listFiles = new File(mp4Path).listFiles();
		String fileName, extName;

		Log.i("getUsbMediaMovieList", "mp4Path : " + mp4Path);

		for (File file : listFiles) {
			fileName = file.getName();
			extName = fileName.substring(fileName.length() - 3);

			if (extName.equals((String) "mp4")) {
				Log.i("getUsbMediaMovieList", "fileName : " + fileName);


				// TODO: mp4 파싱


				String[] cData = new String[6];//0 : DATA (PATH)		1:TITLE				2:BOOKMARK			3:duration			4:size     5:ID

				cData[0] = mp4Path + "/" + fileName;
				cData[1] = fileName.substring(0, fileName.length() - 4);
				cData[2] = "";
				cData[3] = "1";
				cData[4] = "";
				cData[5] = "";

				Log.i("getUsbMediaMovieList", "fileName : " + cData[1]);

				mp4List.add(cData);

			}
		}

		return mp4List;
	}

}
