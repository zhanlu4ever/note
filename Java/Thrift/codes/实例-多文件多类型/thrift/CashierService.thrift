include "./BaseService.thrift"
include "./Cashier.thrift"
include "./Common.thrift"

namespace java com.cbooy.service
namespace py services

typedef Common.Caller				Caller
typedef Common.ResponseMsg			ResponseMsg
typedef Cashier.Order				Order

service CashierService extends BaseService.BaseService{
	ResponseMsg createOrder(1:Caller caller, 2:Order order)
}