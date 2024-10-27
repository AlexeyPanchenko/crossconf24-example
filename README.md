# Example kmp backend with Either usage for typed errors

## Build & Run

**JVM**
```shell
./gradlew :service:jvmBinary
java -jar service.jar
```
or
```shell
./gradlew :service:jvmRun
```

**MacOs**

`X64` or `Arm64`
```shell
./gradlew :service:macosX64Binaries
service.kexe
```
or
```shell
./gradlew :service:runReleaseExecutableMacosX64
```

**Linux**

```shell
./gradlew :service:linuxX64Binaries
service.kexe
```
or
```shell
./gradlew :service:runReleaseExecutableLinuxX64
```

## Api usage

```shell
[POST] /api/v1/apps/{app_id}/release
Content-type: application/json
{
  "version": "String",
  "files": ["String"]
}
```

Example

```shell
curl -v http://0.0.0.0:8080/api/v1/apps/ru.example.app/release \
-X POST \
-H "Content-type: application/json" \
-d '{"version": "12.02", "files": ["file1.apk"]}'
```