namespace java com.cbooy.beans
namespace py beans

typedef i16 common_short
typedef i32 common_int
typedef i64 common_long
typedef bool common_boolean

struct Caller{
	1:required string caller,
	2:optional string requiredId
}

struct UserInfo{
	1: required string             userID
	2: required common_long        accountType
	3: optional string             phone
	4: optional string             userName
	5: optional string             accountID
}

enum RequestType{
	HTTP	=	1,
	HTTPS	=	2,
	THRIFT	=	3,
	DUBBO	=	4
}

struct ResponseMsg{
	1:required string 				errorNO,
	2:required string 				errorMsg,
	3:optional map<string,string> 	extVal
}