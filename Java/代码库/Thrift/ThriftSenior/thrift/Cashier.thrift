include "./Common.thrift"

namespace java com.cbooy.beans
namespace py beans

typedef i16 cashier_short
typedef i32 cashier_int
typedef i64 cashier_long
typedef bool cashier_boolean

struct PayInfo{
	1:required  string 			payId,
	2:required	set<string>		infoSet,
}

struct Order{
	1:required  string 				orderId,
	2:required	Common.UserInfo		userInfo,
	3:optional  list<PayInfo>		payInfos,
}