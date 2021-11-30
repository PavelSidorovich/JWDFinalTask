<%@ page contentType="text/html;charset=UTF-8" %>

<div id="filter" class="col-md-3 order-md-1 mb-4">
    <h4 class="d-flex justify-content-between align-items-center mb-3">
        <span class="text-muted">Filter</span>
        <span id="filterCount" class="badge badge-secondary badge-pill"></span>
    </h4>
    <ul id="filterInputs" class="list-group mb-3">
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">First name</h6>
                <input class="form-control" id="firstName" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">Last name</h6>
                <input class="form-control" id="lastName" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">Phone</h6>
                <input class="form-control" id="phone" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">Discount</h6>
                <input class="form-control" id="discount" type="text">
            </div>
        </li>
        <li class="list-group-item d-flex justify-content-between lh-condensed">
            <div>
                <h6 class="my-0">Expire date</h6>
                <input class="form-control" id="expireDate" type="text">
            </div>
        </li>
    </ul>
</div>
