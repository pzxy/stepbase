package com.base.core.head.constants;

/**
 * @author start 
 */
public interface RegexConstants {
	
	String PHONE="[0-9]+";
	String MOBILE="^(1[3,4,5,6,7,8,9][0-9])\\d{8}$";
	String MAIL="\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";
	String BASE64="^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
	String MD5="[0-9A-Fa-f]+";
	String SHA1="[0-9A-Fa-f]+";
	String IP="[0-9.]+";
	String MAC="[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}";
	String IDCARD="^\\d{15}|^\\d{17}([0-9]|X|x)$";
	
	/**
	 * ETH
	 * 0x04cbb59e340e2097cf4fc4525a3e6b4b014ea842
	 */
	String ETH_ADDRESS = "^0x[a-fA-F0-9]{40}$";
	
    /**
     * ETH HASH
     * 0xdae11cc22922dea0e45da6bd9e090dee4bb2acc1d23a4806de908f7881385669
     */
    String ETH_HASH = "^0x[0-9a-z]{64}$";
	
	/**
	 * FIL f1
	 * f1x6eilf47zki5tipoiozh3a2a3ixff7mdkdoum3i
	 */
	String FIL_ADDRESS_F1 = "^f1[a-z0-9]{39}$";
	
    /**
     * FIL HASH
     * bafy2bzaceahjaizerc6ijtc5i5abnl46n4lit5womzsy2lplrjyrfcv4vltdg
     */
    String FIL_HASH = "^[0-9a-z]{62}$";
	
	/**
	 * TOP
	 * T-0-1Cu1drkubUw6xD6SZRQBytB6YADP6Uxq5K
	 */
	String TOP_ADDRESS = "^T-0-[a-zA-Z0-9]{34}$";
	
	/**
	 * TOP T
	 * T80000bd1bbe214aa35dfdd70fc91b5d40f833296c2f87
	 */
	String TOP_ADDRESS_T = "^T[a-z0-9]{45}$";
    
}
