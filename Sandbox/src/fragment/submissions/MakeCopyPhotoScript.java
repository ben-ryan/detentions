package fragment.submissions;

public class MakeCopyPhotoScript {
	
	// HS 
	// 12 17 37 44 51 53 54 55 56 77 100 120 106 119 124 135 136 137 138 140 153 178 179 197 208 218 225 232 250 255 277 282 284 287 291 418 449 452 453 456 459 465 467 468 469 471 473 480 481 482 484 487 488 497 502 507 537 547 551 561 563 567 582 657 658 663 669 685 697 763 809 840 856 887 897 899 900 901 902 916 918 923 924 933 942 982

	// RC 
	// 12 17 25 37 44 54 74 100 102 106 119 138 140 153 178 179 197 208 218 225 232 250 255 277 282 284 291 307 309 314 354 401 409 416 418 449 465 467 4684 469 470 471 473 479 484 487 488 497 502 507 537 547 551 561 563 567 582 589 597 602 627 657 669 670 685 697 726 763 809 840 856 887 897 899 901 902 918 933 941 942 968 982

	public static void main(String[] photos) {
		
		String family = "Hall-Secretts";
		String dir = family;
		dir = family + "\\blackandwhite" ;
		boolean blackAndWhite = false;
		
		int displacement = 984;
		
		for (String photo : photos) {
			Integer photoNum = Integer.parseInt(photo);
			if(blackAndWhite) {
				photoNum += displacement;
			}
			photo = String.valueOf(photoNum);
			while (photo.length() != 4) {
				photo = "0" + photo;
			}
			System.out.println("copy " + photo + ".jpg \"C:\\Users\\Ben Ryan\\Pictures\\Official Wedding Photos - 6th August 2016\\LowResForWeb-Wedding\\Family Photo Albums\\" + dir + "\\hires\"");
		}

	}

}
