#### What It Is
Mouse Sensitivity adjusts how quickly the pointer moves in response to your physical mouse movement.

#### Hyprland Settings

```
input {
  sensitivity = 0.0
}
```

#### Valid Value

````
Type -> Float
Valid Range -> -1.0 to 1.0
````

#### Tips

- -1.0 is the minimum value and negative values reduce pointer acceleration.
- 1.0 is the maximum value and positive values increase pointer acceleration. 
- If you're a gaming or designer, try -0.5 or 0.0 for precision.
- If you prefer fast desktop movement, try 0.5 or 0.8.

#### Useful Links

[Hyprland Wiki For Input](https://wiki.hypr.land/Configuring/Variables/#input)

[For more technical details see libinput: Pointer Acceleration](https://wayland.freedesktop.org/libinput/doc/latest/pointer-acceleration.html#pointer-acceleration)