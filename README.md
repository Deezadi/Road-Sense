# Road-Sense
An automatic pothole detector for vehicles
# 🚧 PotholeGuard

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android%20%7C%20iOS-blue?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Sensor-Accelerometer%20%7C%20GPS-green?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge" />
</p>

A mobile app that detects potholes while you're driving using your phone's accelerometer. It adjusts sensitivity based on your vehicle type and shows everything live on a graph.

---

## ✨ What it does

- Detects potholes in real time using accelerometer (Z-axis) data
- Adjusts sensitivity based on vehicle type — a bike feels every bump, a bus doesn't
- Live graph on your phone showing sensor readings + pothole events
- GPS tags each detected pothole with location & time
- Saves trip logs you can export as CSV/JSON

---

## 🚗 Vehicle Sensitivity

Different suspensions = different thresholds. Pick your vehicle and the app handles the rest:

| Vehicle | Threshold (m/s²) |
|---|---|
| 🚲 Bicycle | 3.5 |
| 🛵 Motorcycle | 5.0 |
| 🚗 Car | 7.0 |
| 🚙 SUV / Truck | 9.5 |
| 🚌 Bus | 12.0 |

You can also use a custom slider if none of these fit.

---

## 🛠️ Tech Stack

| | |
|---|---|
| Framework | Flutter / React Native |
| Sensors | `sensors_plus` / `react-native-sensors` |
| Graph | `fl_chart` / `Victory Native` |
| Location | `geolocator` / `react-native-geolocation` |

---

## 🔧 Installation

```bash
git clone https://github.com/your-username/potholeguard.git
cd potholeguard

flutter pub get
flutter run
```

> Use a physical device — emulators don't have real accelerometer data.

---

## 🐛 Known Issues

- GPS can be a bit off in areas with tall buildings
- Continuous high-frequency sampling drains battery faster than usual
- Emulator testing needs mocked sensor data

---

## 🙌 Acknowledgements

- [sensors_plus](https://pub.dev/packages/sensors_plus)
- [fl_chart](https://pub.dev/packages/fl_chart)
- [geolocator](https://pub.dev/packages/geolocator)

---

<p align="center">Made with ❤️ for surviving Indian roads</p>
