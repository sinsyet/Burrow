package com.example.burrowlib;

import android.content.Context;
import android.content.Intent;

import com.example.burrowlib.service.BurrowService;

public class Burrow {
    public static void init(Context ctx){
        ctx.startService(new Intent(ctx, BurrowService.class));
    }
}