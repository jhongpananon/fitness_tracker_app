package com.jhongpananon.sqlite_project.Features.UpdateAddressInfo;

import com.jhongpananon.sqlite_project.Features.CreateAddress.Address;

public interface AddressUpdateListener {
    void onAddressInfoUpdated(Address address, int position);
}
