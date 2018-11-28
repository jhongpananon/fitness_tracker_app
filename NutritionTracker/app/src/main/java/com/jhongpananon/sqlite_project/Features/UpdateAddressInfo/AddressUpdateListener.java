package com.jhongpananon.sqlite_project.Features.UpdateAddressInfo;

import com.jhongpananon.sqlite_project.Features.CreateAddress.Exercise;

public interface AddressUpdateListener {
    void onAddressInfoUpdated(Exercise exercise, int position);
}
