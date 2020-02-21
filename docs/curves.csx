#r "System.Drawing.dll"
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;
using System;

delegate double Curve(double x);

static float At(this Curve c, int x) => 126 - (float)c(x / 126.0) * 126f;

var curves = new (string name, Curve c)[]{
    ("linear", x => x),
    ("arc", x => Math.Sqrt(x * (2 - x))),
    ("invarc", x => 1 - Math.Sqrt(1 - x * x)),
    ("sqr", x => x * x),
    ("sqrt", x => Math.Sqrt(x)),
    ("log", x => Math.Log10(1 + x * 9)),
    ("sin", x => (1 - Math.Cos(x * Math.PI)) * 0.5)
};

var f = new Font("Consolas", 9);
var f2 = new Font("Consolas", 9, FontStyle.Bold);
var p = new Pen(Color.Red, 2);
foreach (var (name, c) in curves) {
    using (var b = new Bitmap(192, 170))
    using (var g = Graphics.FromImage(b)) {
        g.Clear(Color.White);
        g.SmoothingMode = SmoothingMode.HighQuality;
        var prevY = c.At(0);
        g.DrawLine(Pens.LightGray, 43, 17, 175, 17);
        g.DrawLine(Pens.LightGray, 175, 17, 175, 150);
        g.DrawLine(Pens.Black, 47, 0, 47, 150);
        g.DrawLine(Pens.Black, 43, 145, 191, 145);
        for (var x = 1; x < 127; ++x) {
            var y = c.At(x);
            g.DrawLine(p, 47 + x, 18 + prevY, 48 + x, 18 + y);
            prevY = y;
        }
        g.DrawString("0", f, Brushes.Black, 43, 146, new StringFormat() {
            Alignment = StringAlignment.Far,
            LineAlignment = StringAlignment.Center
        });
        g.DrawString("0", f, Brushes.Black, 47, 150, new StringFormat() {
            Alignment = StringAlignment.Center,
            LineAlignment = StringAlignment.Near
        });
        g.DrawString("1", f, Brushes.Black, 43, 18, new StringFormat() {
            Alignment = StringAlignment.Far,
            LineAlignment = StringAlignment.Center
        });
        g.DrawString("1", f, Brushes.Black, 176, 150, new StringFormat() {
            Alignment = StringAlignment.Center,
            LineAlignment = StringAlignment.Near
        });
        g.DrawString("x", f2, Brushes.Black, 112, 150, new StringFormat() {
            Alignment = StringAlignment.Center,
            LineAlignment = StringAlignment.Near
        });
        g.DrawString("value", f2, Brushes.Black, 43, 82, new StringFormat() {
            Alignment = StringAlignment.Far,
            LineAlignment = StringAlignment.Center
        });
        b.Save($"curve-{name}.png", ImageFormat.Png);
    }
}
