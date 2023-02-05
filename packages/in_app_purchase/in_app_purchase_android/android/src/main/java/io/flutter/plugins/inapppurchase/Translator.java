// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.inapppurchase;

import androidx.annotation.Nullable;
import com.android.billingclient.api.AccountIdentifiers;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.SkuDetails;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/** Handles serialization of {@link com.android.billingclient.api.BillingClient} related objects. */
/*package*/ class Translator {
  static HashMap<String, Object> fromSkuDetail(ProductDetails detail) {
    // TODO: Look here later
    HashMap<String, Object> info = new HashMap<>();
    info.put("title", detail.getTitle());
    info.put("description", detail.getDescription());
    info.put("freeTrialPeriod", null);
    info.put("introductoryPrice", detail.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice());
    info.put("introductoryPriceAmountMicros", detail.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getPriceAmountMicros());
    info.put("introductoryPriceCycles", null);
    info.put("introductoryPricePeriod", null);
    info.put("price", detail.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice());
    info.put("priceAmountMicros", detail.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getPriceAmountMicros());
    info.put("priceCurrencyCode", detail.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getPriceCurrencyCode());
    info.put("priceCurrencySymbol", detail.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getPriceCurrencyCode());
    info.put("sku", detail.getProductId());
    info.put("type", detail.getProductType());
    info.put("subscriptionPeriod", detail.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getBillingPeriod());
    info.put("originalPrice", detail.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice());
    info.put("originalPriceAmountMicros", detail.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getPriceAmountMicros());
    return info;
  }

  static List<HashMap<String, Object>> fromSkuDetailsList(
      @Nullable List<ProductDetails> skuDetailsList) {
    if (skuDetailsList == null) {
      return Collections.emptyList();
    }

    ArrayList<HashMap<String, Object>> output = new ArrayList<>();
    for (ProductDetails detail : skuDetailsList) {
      output.add(fromSkuDetail(detail));
    }
    return output;
  }

  static HashMap<String, Object> fromPurchase(Purchase purchase) {
    HashMap<String, Object> info = new HashMap<>();
    List<String> skus = purchase.getSkus();
    info.put("orderId", purchase.getOrderId());
    info.put("packageName", purchase.getPackageName());
    info.put("purchaseTime", purchase.getPurchaseTime());
    info.put("purchaseToken", purchase.getPurchaseToken());
    info.put("signature", purchase.getSignature());
    info.put("skus", skus);
    info.put("isAutoRenewing", purchase.isAutoRenewing());
    info.put("originalJson", purchase.getOriginalJson());
    info.put("developerPayload", purchase.getDeveloperPayload());
    info.put("isAcknowledged", purchase.isAcknowledged());
    info.put("purchaseState", purchase.getPurchaseState());
    info.put("quantity", purchase.getQuantity());
    AccountIdentifiers accountIdentifiers = purchase.getAccountIdentifiers();
    if (accountIdentifiers != null) {
      info.put("obfuscatedAccountId", accountIdentifiers.getObfuscatedAccountId());
      info.put("obfuscatedProfileId", accountIdentifiers.getObfuscatedProfileId());
    }
    return info;
  }

  static HashMap<String, Object> fromPurchaseHistoryRecord(
      PurchaseHistoryRecord purchaseHistoryRecord) {
    HashMap<String, Object> info = new HashMap<>();
    List<String> skus = purchaseHistoryRecord.getSkus();
    info.put("purchaseTime", purchaseHistoryRecord.getPurchaseTime());
    info.put("purchaseToken", purchaseHistoryRecord.getPurchaseToken());
    info.put("signature", purchaseHistoryRecord.getSignature());
    info.put("skus", skus);
    info.put("developerPayload", purchaseHistoryRecord.getDeveloperPayload());
    info.put("originalJson", purchaseHistoryRecord.getOriginalJson());
    info.put("quantity", purchaseHistoryRecord.getQuantity());
    return info;
  }

  static List<HashMap<String, Object>> fromPurchasesList(@Nullable List<Purchase> purchases) {
    if (purchases == null) {
      return Collections.emptyList();
    }

    List<HashMap<String, Object>> serialized = new ArrayList<>();
    for (Purchase purchase : purchases) {
      serialized.add(fromPurchase(purchase));
    }
    return serialized;
  }

  static List<HashMap<String, Object>> fromPurchaseHistoryRecordList(
      @Nullable List<PurchaseHistoryRecord> purchaseHistoryRecords) {
    if (purchaseHistoryRecords == null) {
      return Collections.emptyList();
    }

    List<HashMap<String, Object>> serialized = new ArrayList<>();
    for (PurchaseHistoryRecord purchaseHistoryRecord : purchaseHistoryRecords) {
      serialized.add(fromPurchaseHistoryRecord(purchaseHistoryRecord));
    }
    return serialized;
  }

  static HashMap<String, Object> fromBillingResult(BillingResult billingResult) {
    HashMap<String, Object> info = new HashMap<>();
    info.put("responseCode", billingResult.getResponseCode());
    info.put("debugMessage", billingResult.getDebugMessage());
    return info;
  }

  /**
   * Gets the symbol of for the given currency code for the default {@link Locale.Category#DISPLAY
   * DISPLAY} locale. For example, for the US Dollar, the symbol is "$" if the default locale is the
   * US, while for other locales it may be "US$". If no symbol can be determined, the ISO 4217
   * currency code is returned.
   *
   * @param currencyCode the ISO 4217 code of the currency
   * @return the symbol of this currency code for the default {@link Locale.Category#DISPLAY
   *     DISPLAY} locale
   * @exception NullPointerException if <code>currencyCode</code> is null
   * @exception IllegalArgumentException if <code>currencyCode</code> is not a supported ISO 4217
   *     code.
   */
  static String currencySymbolFromCode(String currencyCode) {
    return Currency.getInstance(currencyCode).getSymbol();
  }
}
